package com.zfx.service;

import com.macro.mall.model.OmsOrder;
import com.macro.mall.model.OmsOrderExample;

import java.util.List;

public interface IOrderService {
     List<OmsOrder> getOrderById(Long id);

     List<OmsOrder>  getOrderByUsername(String username);
}
