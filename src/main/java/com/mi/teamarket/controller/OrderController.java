package com.mi.teamarket.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mi.teamarket.entity.*;
import com.mi.teamarket.mapper.*;
import com.mi.teamarket.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private TeaProductMapper teaProductMapper;

    @Autowired
    private FlashSaleMapper flashSaleMapper;

    @Autowired
    private TodaySaleController todaySaleController;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ComplaintMapper complaintMapper;

    @Autowired
    private OrderExtensionMapper orderExtensionMapper;

    @PostMapping("/settle-order/{user_id}")
    public Status settleOrder(@PathVariable("user_id") Integer userId) {

        // 找到用户购物车中的所有 已选择的商品
        QueryWrapper<ShoppingCart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("is_selected", true);
        queryWrapper.eq("is_valid", true);
        var shoppingCarts = shoppingCartMapper.selectList(queryWrapper);

        if (shoppingCarts.isEmpty()) {
            return Status.getFailureInstance("购物车暂无被选择的商品");
        }

        var newOrder = new Order(userId, 0, BigDecimal.ZERO);
        orderMapper.insertOrUpdate(newOrder); // 插入后数据回填到 newOrder 中，拿到orderID


        // 设置订单内产品总数
        newOrder.setTotalNum(shoppingCarts.size());

        // 计算订单总金额
        BigDecimal total = BigDecimal.ZERO;
        for (var item : shoppingCarts) {
            item.setDiscount(todaySaleController.getTodaySaleById(item.getProductId()));
            var teaProduct = teaProductMapper.selectById(item.getProductId());
            if (item.getSpecialPrice() == null) {
                total = total.add(teaProduct.getPrice().multiply(item.getQuantity()).multiply(item.getDiscount()));
            } else {
                total = total.add(item.getQuantity().multiply(item.getSpecialPrice()));
            }
            item.setIsValid(false);
            item.setOrderId(newOrder.getOrderId());
        }
        shoppingCartMapper.insertOrUpdate(shoppingCarts);

        total = total.setScale(2, RoundingMode.HALF_UP);
        newOrder.setTotalAmount(total);
        orderMapper.insertOrUpdate(newOrder);

        var oe = new OrderExtension();
        oe.setOrderId(newOrder.getOrderId());
        oe.setAddress(userMapper.selectById(userId).getAddress());
        orderExtensionMapper.insertOrUpdate(oe);

        return Status.getSuccessInstance();
    }

    @PostMapping("/settle-one-order/{sc_id}")
    public Status settleOneOrder(@PathVariable("sc_id") Integer Id) {
        // 找到购物车项目，创建订单
        var sc = shoppingCartMapper.selectById(Id);
        var newOrder = new Order(sc.getUserId(), 0, BigDecimal.ZERO);
        orderMapper.insertOrUpdate(newOrder); // 插入后数据回填到 newOrder 中，拿到orderID

        var discount = todaySaleController.getTodaySaleById(sc.getProductId());

        // 计算订单总金额 并更新购物车
        BigDecimal total = BigDecimal.ZERO;

        var teaProduct = teaProductMapper.selectById(sc.getProductId());
        if (sc.getSpecialPrice() == null) {
            total = total.add(teaProduct.getPrice().multiply(sc.getQuantity()).multiply(sc.getDiscount()));
        } else {
            total = total.add(sc.getQuantity().multiply(sc.getSpecialPrice()));
        }

        sc.setIsValid(false);
        sc.setDiscount(discount);
        sc.setOrderId(newOrder.getOrderId());
        shoppingCartMapper.insertOrUpdate(sc);

        // 修改订单相关配置
        newOrder.setTotalNum(1);
        total = total.setScale(2, RoundingMode.HALF_UP);
        newOrder.setTotalAmount(total);
        orderMapper.insertOrUpdate(newOrder);

        var oe = new OrderExtension();
        oe.setOrderId(newOrder.getOrderId());
        oe.setAddress(userMapper.selectById(sc.getUserId()).getAddress());
        orderExtensionMapper.insertOrUpdate(oe);

        return Status.getSuccessInstance();
    }


    @GetMapping("/get-all/{user_id}/{status}")
    public List<OrderInstance> getAllOrders(@PathVariable("user_id") Integer userId, @PathVariable("status") Integer status) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        if (status >= 0 && status <= 5) queryWrapper.eq("status", OrderStatus.is(status));
        var orders = orderMapper.selectList(queryWrapper);
        List<OrderInstance> lo = new ArrayList<>();
        for (var item : orders) {
            ProductInfo pi;
            QueryWrapper<ShoppingCart> qw1 = new QueryWrapper<>();
            qw1.eq("user_id", userId);
            qw1.eq("order_id", item.getOrderId());
            qw1.eq("is_valid", false);
            var itemsInThisSC = shoppingCartMapper.selectList(qw1);
            var pi_list = new ArrayList<ProductInfo>();
            for (var x : itemsInThisSC) {
                var curr = teaProductMapper.selectById(x.getProductId());
                if (x.getSpecialPrice() == null) {
                    pi = new ProductInfo(curr.getProductName(), curr.getDescription(), x.getQuantity(), curr.getPrice(), x.getDiscount(), null, null, x.getPackageStyle());
                } else {
                    pi = new ProductInfo(curr.getProductName(), curr.getDescription(), x.getQuantity(), curr.getPrice(), x.getDiscount(), x.getSpecialPrice(), x.getLimitation(), x.getPackageStyle());
                }
                pi_list.add(pi);
            }
            var oe = orderExtensionMapper.selectOne(new QueryWrapper<OrderExtension>().eq("order_id", item.getOrderId()));
            var no = new OrderInstance(item.getOrderId(),
                    item.getUserId(),
                    item.getCreationTime(),
                    item.getSettlementTime(),
                    pi_list,
                    item.getTotalNum(),
                    item.getTotalAmount(),
                    oe != null ? oe.getAddress() : "未知地址",
                    oe != null ? oe.getExpressName() : "未知快递公司",
                    oe != null ? oe.getExpressNumber() : "未知快递单号",
                    item.getStatus(),
                    item.isComplained());
            lo.add(no);
        }
        return lo;
    }

    @PostMapping("/modify-order-status/{order_id}/{status}")
    public Status modifyOrderStatus(@PathVariable("order_id") Integer orderId, @PathVariable("status") String status_str) {
        int status = Integer.parseInt(status_str);
        if (status < 0 || status > 5) return Status.getFailureInstance();
        if (status == OrderStatus.ORDER_CANCELLED) {
            QueryWrapper<ShoppingCart> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("order_id", orderId);
            var scList = shoppingCartMapper.selectList(queryWrapper);
            for (var x : scList) {
                x.setShoppingCartId(null);
                x.setIsValid(true);
            }
            shoppingCartMapper.insert(scList);
        }

        var order = orderMapper.selectById(orderId);
        order.setStatus(OrderStatus.is(status));
        orderMapper.insertOrUpdate(order);
        return Status.getSuccessInstance();
    }

    @PostMapping("/finish-order/{order_id}")
    public Status finishOrder(@PathVariable("order_id") Integer orderId) {
        var theOrder = orderMapper.selectById(orderId);
        theOrder.setSettlementTime(Utility.getCurrentTimeString());
        theOrder.setStatus(OrderStatus.is(OrderStatus.FINISHED));
        orderMapper.insertOrUpdate(theOrder);
        return Status.getSuccessInstance();
    }

    // 支付
    @PostMapping("/pay-the-order/{order_id}")
    @Transactional(rollbackFor = Exception.class)
    public Status payOrder(@PathVariable("order_id") Integer id) {
        var order = orderMapper.selectById(id);

        QueryWrapper<ShoppingCart> qw1 = new QueryWrapper<>();
        qw1.eq("user_id", order.getUserId());
        qw1.eq("order_id", order.getOrderId());
        qw1.eq("is_valid", false);
        var items = shoppingCartMapper.selectList(qw1);

        // 检查库存是否足够
        for (var x : items) {
            if (x.getSpecialPrice() == null) {
                var tp = teaProductMapper.selectById(x.getProductId());
                if (tp.getStock().compareTo(x.getQuantity()) < 0) {
                    throw new RuntimeException("库存不够扣减"); // 库存不足，抛出异常触发事务回滚
                }
            } else {
                QueryWrapper<FlashSale> qw = new QueryWrapper<>();
                qw.eq("product_id", x.getProductId());
                qw.eq("special_price", x.getSpecialPrice());
                qw.eq("person_limitation", x.getLimitation());
                var fs = flashSaleMapper.selectOne(qw);
                if (fs.getSpecialStock().compareTo(x.getQuantity()) < 0)
                    throw new RuntimeException("库存不够扣减"); // 库存不足，抛出异常触发事务回滚
            }

        }

        // 库存足够，进行批量扣减
        for (var x : items) {
            if (x.getSpecialPrice() == null) {
                var tp = teaProductMapper.selectById(x.getProductId());
                tp.setStock(tp.getStock().subtract(x.getQuantity()));
                teaProductMapper.insertOrUpdate(tp);
            } else {
                QueryWrapper<FlashSale> qw = new QueryWrapper<>();
                qw.eq("product_id", x.getProductId());
                qw.eq("special_price", x.getSpecialPrice());
                qw.eq("person_limitation", x.getLimitation());
                var fs = flashSaleMapper.selectOne(qw);
                fs.setSpecialStock(fs.getSpecialStock().subtract(x.getQuantity()));
                flashSaleMapper.insertOrUpdate(fs);
            }
        }

        this.modifyOrderStatus(order.getOrderId(), String.valueOf(OrderStatus.AWAITING_DELIVERY));
        return Status.getSuccessInstance();
    }

    @PostMapping("/get-all/{status}")
    public List<OrderStruct> getOrders(@PathVariable("status") Integer status, @RequestParam Boolean complained) {
        List<OrderStruct> orderStructList = new ArrayList<>();
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        if (status >= 0 && status <= 5) queryWrapper.eq("status", OrderStatus.is(status));
        else {
            queryWrapper.ne("status", OrderStatus.is(OrderStatus.ORDER_CANCELLED));
            queryWrapper.ne("status", OrderStatus.is(OrderStatus.ORDER_TIMEOUT));
            queryWrapper.ne("status", OrderStatus.is(OrderStatus.PENDING_PAYMENT));
        }
        var orders = orderMapper.selectList(queryWrapper);
        for (var item : orders) {
            var os = new OrderStruct();
            ProductInfo pi;
            QueryWrapper<ShoppingCart> qw1 = new QueryWrapper<>();
            qw1.eq("user_id", item.getUserId());
            qw1.eq("order_id", item.getOrderId());
            qw1.eq("is_valid", false);
            var itemsInThisSC = shoppingCartMapper.selectList(qw1);
            var pi_list = new ArrayList<ProductInfo>();
            for (var x : itemsInThisSC) {
                var curr = teaProductMapper.selectById(x.getProductId());
                if (x.getSpecialPrice() == null) {
                    pi = new ProductInfo(curr.getProductName(), curr.getDescription(), x.getQuantity(), curr.getPrice(), x.getDiscount(), null, null, x.getPackageStyle());
                } else {
                    pi = new ProductInfo(curr.getProductName(), curr.getDescription(), x.getQuantity(), curr.getPrice(), x.getDiscount(), x.getSpecialPrice(), x.getLimitation(), x.getPackageStyle());
                }
                pi_list.add(pi);
            }
            // todo 加入快递公司名 又该改了
            var oe = orderExtensionMapper.selectOne(new QueryWrapper<OrderExtension>().eq("order_id", item.getOrderId()));
            os.setOrderInstance(
                    new OrderInstance(
                            item.getOrderId(),
                            item.getUserId(),
                            item.getCreationTime(),
                            item.getSettlementTime(),
                            pi_list, item.getTotalNum(),
                            item.getTotalAmount(),
                            oe != null ? oe.getAddress() : "未知地址",
                            oe != null ? oe.getExpressName() : "未知快递公司",
                            oe != null ? oe.getExpressNumber() : "未知快递单号",
                            item.getStatus(),
                            item.isComplained()
                    ));
            os.setPhoneNumber(userMapper.selectById(item.getUserId()).getPhoneNumber());
            if (oe != null) {
                os.setUserAddress(oe.getAddress());
                os.setExpressName(oe.getExpressName());
                os.setExpressNumber(oe.getExpressNumber());
            }
            if (item.isComplained()) {
                os.setComplaintRecord(complaintMapper.selectOne(new QueryWrapper<Complaint>().eq("order_id", item.getOrderId())).getContent());
                os.setReply(complaintMapper.selectOne(new QueryWrapper<Complaint>().eq("order_id", item.getOrderId())).getReply());
            }
            orderStructList.add(os);
        }
        if (complained)
            orderStructList.removeIf(orderStruct -> !orderStruct.getOrderInstance().isComplained());

        return orderStructList;
    }

    @PostMapping("/ship")
    public Status ship(@RequestParam Integer orderId, @RequestParam String expressName, @RequestParam String expressNumber) {
        var oe = orderExtensionMapper.selectOne(new QueryWrapper<OrderExtension>().eq("order_id", orderId));
        if (oe == null) oe = new OrderExtension();
        oe.setExpressNumber(expressNumber);
        oe.setExpressName(expressName);
        oe.setOrderId(orderId);
        orderExtensionMapper.insertOrUpdate(oe);
        modifyOrderStatus(orderId, String.valueOf(OrderStatus.DELIVERED_AWAITING_CONFIRMATION));
        return Status.getSuccessInstance("成功发货");
    }

}
