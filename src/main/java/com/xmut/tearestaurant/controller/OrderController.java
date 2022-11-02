package com.xmut.tearestaurant.controller;

import com.xmut.tearestaurant.common.R;
import com.xmut.tearestaurant.entity.Orders;
import com.xmut.tearestaurant.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Bless_Wu
 * @Description 结算功能的实现
 * @create 2022-11-02 16:44
 */
@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info(orders.toString());
        orderService.submit(orders);
        return R.success("成功");
    }
}
