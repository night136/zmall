package com.zfx.service;

import com.macro.mall.mapper.OmsOrderMapper;
import com.macro.mall.model.OmsOrder;
import com.macro.mall.model.OmsOrderExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService implements IOrderService {
    @Autowired
    OmsOrderMapper OrderMapper;
    public  List<OmsOrder> getOrderById(Long id) {
        OmsOrderExample example = new OmsOrderExample();
        example.createCriteria().andIdEqualTo(id);
        return OrderMapper.selectByExample(example);
    }

    @Override
    public List<OmsOrder> getOrderByUsername(String username) {
        OmsOrderExample example=new OmsOrderExample();
        example.createCriteria().andMemberUsernameEqualTo(username);
        return OrderMapper.selectByExample(example);
    }


}
