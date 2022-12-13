package com.xmut.tearestaurant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xmut.tearestaurant.common.BaseContext;
import com.xmut.tearestaurant.common.CustomException;
import com.xmut.tearestaurant.entity.*;
import com.xmut.tearestaurant.mapper.OrderMapper;
import com.xmut.tearestaurant.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Bless_Wu
 * @Description
 * @create 2022-11-02 16:41
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {

    @Autowired
    private ShoppingService shoppingService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 用户下单
     *
     * @param orders
     */
    @Transactional
    @Override
    public void submit(Orders orders) {
        //1.获取当前用户
//        Long currentId = BaseContext.getCurrentId();
        //2.获取当前用户的购物车数据
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, orders.getUserId());
        List<ShoppingCart> shoppingCarts = shoppingService.list(queryWrapper);
        if (shoppingCarts == null || shoppingCarts.size() == 0) {
            throw new CustomException("购物车为空 不能下单");
        }
        //查询用户数据
        User user = userService.getById(orders.getUserId());
        //查询地址数据
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if (addressBook == null) {
            throw new CustomException("地址不能为空");
        }
        //3.向订单表中插入一条数据
        long orderId = IdWorker.getId();//订单号

        AtomicInteger amount = new AtomicInteger(0);
        List<OrderDetail> orderDetails = shoppingCarts.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            //累加  具体为amount+=新金额*分数
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;

        }).collect(Collectors.toList());


        orders.setNumber(String.valueOf(orderId));
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);

        orders.setUserName(user.getName());
        orders.setAmount(new BigDecimal(amount.get()));
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName()) +
                (addressBook.getCityName() == null ? "" : addressBook.getCityName()) +
                (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName()) +
                (addressBook.getDetail() == null ? "" : addressBook.getDetail())
        );
        this.save(orders);
        //4.向订单明细表中插入多条数据
        orderDetailService.saveBatch(orderDetails);
        //清空购物车的数据
        shoppingService.remove(queryWrapper);
    }

    @Override
    public void saveSelfOrder(Orders orders) {
        Date date = new Date();
        long time = date.getHours();
        char a = (char) (65 + time / 2);
        int minutes = date.getMinutes();
        int seconds = date.getSeconds();
        String s = a + "" + minutes + "" + seconds;
        //设置取单号
        orders.setRemark(s);
        //2.获取当前用户的购物车数据
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, orders.getUserId());
        List<ShoppingCart> shoppingCarts = shoppingService.list(queryWrapper);
        if (shoppingCarts == null || shoppingCarts.size() == 0) {
            throw new CustomException("购物车为空 不能下单");
        }
        //查询用户数据
        User user = userService.getById(orders.getUserId());
        //3.向订单表中插入一条数据
        long orderId = IdWorker.getId();//订单号
        AtomicInteger amount = new AtomicInteger(0);
        List<OrderDetail> orderDetails = shoppingCarts.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            //累加  具体为amount+=新金额*分数
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;

        }).collect(Collectors.toList());


        orders.setNumber(String.valueOf(orderId));
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        //订单状态 1待制作，2待派送，3派送中，4待取货，5已完成
        orders.setStatus(1);

        orders.setUserName(user.getName());
        orders.setAmount(new BigDecimal(amount.get()));

        orders.setPhone(orders.getPhone());

        this.save(orders);
        //4.向订单明细表中插入多条数据
        orderDetailService.saveBatch(orderDetails);
        //清空购物车的数据
        shoppingService.remove(queryWrapper);
    }
}
