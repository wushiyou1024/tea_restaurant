package com.xmut.tearestaurant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xmut.tearestaurant.entity.Orders;

/**
 * @author Bless_Wu
 * @Description
 * @create 2022-11-02 16:41
 */
public interface OrderService extends IService<Orders> {

    void submit(Orders orders);
}
