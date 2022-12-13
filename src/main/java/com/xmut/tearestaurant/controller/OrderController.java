package com.xmut.tearestaurant.controller;

import com.alibaba.druid.sql.visitor.functions.If;
import com.xmut.tearestaurant.common.R;
import com.xmut.tearestaurant.entity.Orders;
import com.xmut.tearestaurant.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Bless_Wu
 * @Description 结算功能的实现
 * @create 2022-11-02 16:44
 */
@RestController
@Slf4j
@RequestMapping("/order")
@CrossOrigin
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     *
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        log.info(orders.toString());
        if (orders.getRemark()!=null&&orders.getRemark().equals("self")) {
            //自取订单
            orderService.saveSelfOrder(orders);
        } else {
            orderService.submit(orders);
        }
        return R.success("成功");
    }
}
