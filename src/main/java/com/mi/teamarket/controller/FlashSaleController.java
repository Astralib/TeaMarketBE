package com.mi.teamarket.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mi.teamarket.entity.FlashSale;
import com.mi.teamarket.entity.Status;
import com.mi.teamarket.entity.TeaProductForFlashSale;
import com.mi.teamarket.mapper.FlashSaleMapper;
import com.mi.teamarket.mapper.TeaProductMapper;
import com.mi.teamarket.mapper.UserMapper;
import com.mi.teamarket.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/fs")
public class FlashSaleController {
    @Autowired
    private FlashSaleMapper flashSaleMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TeaProductMapper teaProductMapper;

    @GetMapping("/getFlashSaleProductsByVideoId/{id}")
    public List<TeaProductForFlashSale> getFlashSaleProducts(@PathVariable Integer id) {
        List<TeaProductForFlashSale> return_list = new ArrayList<>();
        var flashSaleList = flashSaleMapper.selectList(new QueryWrapper<FlashSale>().eq("video_id", id));

        for (var x : flashSaleList) {
            x.setValid(Utility.isCurrentTimeBetweenDates(x.getStartTime(), x.getEndTime()));
        }
        flashSaleList.removeIf(obj -> !obj.isValid());

        for (var fs : flashSaleList) {
            var tp = teaProductMapper.selectById(fs.getProductId());
            return_list.add(new TeaProductForFlashSale(tp, fs));
        }
        return return_list;
    }

    @GetMapping("/getFlashSalesByVideoId/{id}")
    public List<FlashSale> getFlashSales(@PathVariable Integer id) {
        var l = flashSaleMapper.selectList(new QueryWrapper<FlashSale>().eq("video_id", id));
        for (var x : l) {
            x.setReleaserName(userMapper.selectById(x.getReleaserId()).getUsername());
            x.setProductName(teaProductMapper.selectById(x.getProductId()).getProductName());
        }
        return l;
    }

    @PostMapping("/updateFlashSale/{fsId}")
    public Status updateFlashSale(@PathVariable Integer fsId,
                                  @RequestParam BigDecimal specialPrice,
                                  @RequestParam BigDecimal specialStock,
                                  @RequestParam BigDecimal personLimitation,
                                  @RequestParam String startTime,
                                  @RequestParam String endTime) {
        var fs = flashSaleMapper.selectById(fsId);
        fs.setSpecialPrice(specialPrice);
        fs.setSpecialStock(specialStock);
        fs.setPersonLimitation(personLimitation);
        fs.setStartTime(Utility.parseDate(startTime));
        if (Utility.parseDate(endTime) == null) return Status.getFailureInstance();
        fs.setEndTime(Utility.parseDate(endTime));
        flashSaleMapper.insertOrUpdate(fs);
        return Status.getSuccessInstance();
    }


    @PostMapping("/insertFlashSale/{videoId}")
    public Status insertFlashSale(@PathVariable Integer videoId,
                                  @RequestParam Integer userId,
                                  @RequestParam Integer productId,
                                  @RequestParam BigDecimal specialPrice,
                                  @RequestParam BigDecimal specialStock,
                                  @RequestParam BigDecimal personLimitation,
                                  @RequestParam String startTime,
                                  @RequestParam String endTime) {
        var fs = new FlashSale();
        fs.setReleaserId(userId);
        fs.setProductId(productId);
        fs.setSpecialPrice(specialPrice);
        fs.setSpecialStock(specialStock);
        fs.setPersonLimitation(personLimitation);
        fs.setStartTime(Utility.parseDate(startTime));
        fs.setVideoId(videoId);
        fs.setEndTime(Utility.parseDate(endTime));
        flashSaleMapper.insertOrUpdate(fs);
        return Status.getSuccessInstance();
    }

    @PostMapping("/removeFlashSale/{fsId}")
    public Status removeFlashSale(@PathVariable Integer fsId) {
        flashSaleMapper.deleteById(fsId);
        return Status.getSuccessInstance("移除成功");
    }

    @PostMapping("/getFlashSaleById/{fsId}")
    public FlashSale getFlashSaleById(@PathVariable Integer fsId) {
        var fs = flashSaleMapper.selectById(fsId);
        fs.setProductName(teaProductMapper.selectById(fs.getProductId()).getProductName());
        return fs;
    }

    @GetMapping("/extendEndTime/{fsId}/{hours}")
    public Status extendEndTime(@PathVariable Integer fsId, @PathVariable Integer hours) {
        var fs = flashSaleMapper.selectById(fsId);
        var date = fs.getEndTime();
        LocalDateTime localDateTime = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        LocalDateTime newDateTime = localDateTime.plusHours(hours); // 增加小时
        fs.setEndTime(Date.from(newDateTime.atZone(ZoneId.systemDefault()).toInstant()));
        flashSaleMapper.insertOrUpdate(fs);
        return Status.getSuccessInstance("时间延长成功");
    }


}
