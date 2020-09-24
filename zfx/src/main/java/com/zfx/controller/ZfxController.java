package com.zfx.controller;


import com.macro.mall.common.api.CommonResult;
import com.macro.mall.model.OmsOrder;
import com.zfx.service.HomeService;
import com.zfx.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ZfxController {
    @Autowired
    private IOrderService orderService;
    @Autowired
    private HomeService homeService;

    /*根据id查询订单*/
    @RequestMapping(value = "/updateorder/{id}", method = RequestMethod.GET)
    public CommonResult getOrderById(@PathVariable Long id) {
        List<OmsOrder> omsOrderList = orderService.getOrderById(id);
        return CommonResult.success(omsOrderList);
    }

    @PostMapping("/getAllOrders/{username}")
    public CommonResult getOrderByUsername(@PathVariable String username) {
        return CommonResult.success(orderService.getOrderByUsername(username));
    }

    @GetMapping("/getContent")
    public CommonResult getcontent() {
        return CommonResult.success(homeService.content());

    }
}