package com.mi.teamarket.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mi.teamarket.entity.*;
import com.mi.teamarket.mapper.OrderMapper;
import com.mi.teamarket.mapper.ShoppingCartMapper;
import com.mi.teamarket.mapper.TeaProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private TeaProductMapper teaProductMapper;

    @PostMapping("/settle-order/{user_id}")
    public Status settleOrder(@PathVariable("user_id") Integer userId) {
        var newOrder = new Order(userId, 0, BigDecimal.ZERO);
        orderMapper.insertOrUpdate(newOrder); // 插入后数据回填到 newOrder 中，拿到orderID

        // 找到用户购物车中的所有 已选择的商品
        QueryWrapper<ShoppingCart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("is_selected", true);
        queryWrapper.eq("is_valid", true);
        var shoppingCarts = shoppingCartMapper.selectList(queryWrapper);

        // 设置订单内产品总数
        newOrder.setTotalNum(shoppingCarts.size());

        // 计算订单总金额
        BigDecimal total = BigDecimal.ZERO;
        for (var item : shoppingCarts) {
            var teaProduct = teaProductMapper.selectById(item.getProductId());
            total = total.add(teaProduct.getPrice().multiply(item.getQuantity()));
            item.setIsValid(false);
            item.setOrderId(newOrder.getOrderId());
        }
        shoppingCartMapper.insertOrUpdate(shoppingCarts);

        total = total.setScale(2, RoundingMode.HALF_UP);
        newOrder.setTotalAmount(total);
        orderMapper.insertOrUpdate(newOrder);

        // 设置15分钟后订单超时
        scheduler.schedule(() -> {
            newOrder.setStatus(OrderStatus.is(OrderStatus.ORDER_TIMEOUT));
            orderMapper.insertOrUpdate(newOrder);
        }, 15, TimeUnit.MINUTES);

        return Status.getSuccessInstance();
    }

    @GetMapping("/get-all/{user_id}/{status}")
    public List<OrderInstance> getAllOrders(@PathVariable("user_id") Integer userId, @PathVariable("status") int status) {
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
                pi = new ProductInfo(curr.getProductName(), curr.getDescription(), x.getQuantity(), curr.getPrice(), x.getPackageStyle());
                pi_list.add(pi);
            }
            var no = new OrderInstance(item.getOrderId(), item.getUserId(), item.getCreationTime(), item.getSettlementTime(), pi_list, item.getTotalNum(), item.getTotalAmount(), item.getStatus());
            lo.add(no);
        }

        return lo;
    }

    @PostMapping("/modify-order-status/{order_id}/{status}")
    public Status modifyOrderStatus(@PathVariable("order_id") Integer orderId, @PathVariable("status") int status) {
        if (status < 0 || status > 5) return Status.getFailureInstance();
        var order = orderMapper.selectById(orderId);
        order.setStatus(OrderStatus.is(status));
        orderMapper.insertOrUpdate(order);
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
            var tp = teaProductMapper.selectById(x.getProductId());
            if (tp.getStock().compareTo(x.getQuantity()) < 0) {
                throw new RuntimeException("库存不够扣减"); // 库存不足，抛出异常触发事务回滚
            }
        }

        // 库存足够，进行批量扣减
        for (var x : items) {
            var tp = teaProductMapper.selectById(x.getProductId());
            tp.setStock(tp.getStock().subtract(x.getQuantity()));
            teaProductMapper.insertOrUpdate(tp);
        }

        this.modifyOrderStatus(order.getOrderId(), OrderStatus.AWAITING_DELIVERY);
        return Status.getSuccessInstance();
    }

}
