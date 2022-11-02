package com.xmut.tearestaurant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xmut.tearestaurant.entity.OrderDetail;
import com.xmut.tearestaurant.mapper.OrderDetailMapper;
import com.xmut.tearestaurant.service.OrderDetailService;
import com.xmut.tearestaurant.service.OrderService;
import org.springframework.stereotype.Service;

/**
 * @author Bless_Wu
 * @Description
 * @create 2022-11-02 16:43
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
