package com.xmut.tearestaurant.controller;

import com.alibaba.druid.sql.visitor.functions.If;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xmut.tearestaurant.common.R;
import com.xmut.tearestaurant.dto.OrdersDto;
import com.xmut.tearestaurant.entity.OrderDetail;
import com.xmut.tearestaurant.entity.Orders;
import com.xmut.tearestaurant.service.OrderDetailService;
import com.xmut.tearestaurant.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    OrderDetailService orderDetailService;

    /**
     * 用户下单
     *
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        log.info(orders.toString());
        if (orders.getRemark() != null && orders.getRemark().equals("self")) {
            //自取订单
            orderService.saveSelfOrder(orders);
        } else {
            orderService.submit(orders);
        }
        return R.success("成功");
    }


    @GetMapping("/getByUser")
    public R<List<OrdersDto>> getByUser(Long userid) {
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, userid);
        List<Orders> ordersList = orderService.list(queryWrapper);

        List<OrdersDto> ordersDtoList = ordersList.stream().map((item) -> {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item, ordersDto);
            LambdaQueryWrapper<OrderDetail> detailLambdaQueryWrapper = new LambdaQueryWrapper<>();
            detailLambdaQueryWrapper.eq(OrderDetail::getOrderId, item.getNumber());
            List<OrderDetail> orderDetailList = orderDetailService.list(detailLambdaQueryWrapper);
            ordersDto.setOrdersList(orderDetailList);
            return ordersDto;
        }).collect(Collectors.toList());

        return R.success(ordersDtoList);
    }


    @GetMapping("getDetail")
    public R<OrdersDto> getDetail(Long orderid) {
//        System.out.println(orderid);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getNumber, orderid);
        Orders orders = orderService.getOne(queryWrapper);
        OrdersDto ordersDto = new OrdersDto();
        BeanUtils.copyProperties(orders, ordersDto);
        LambdaQueryWrapper<OrderDetail> detailLambdaQueryWrapper = new LambdaQueryWrapper<>();
        detailLambdaQueryWrapper.eq(OrderDetail::getOrderId, orderid);
        List<OrderDetail> orderDetailList = orderDetailService.list(detailLambdaQueryWrapper);
        ordersDto.setOrdersList(orderDetailList);
        return R.success(ordersDto);
    }
}
