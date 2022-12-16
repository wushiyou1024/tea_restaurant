package com.xmut.tearestaurant.dto;

import com.xmut.tearestaurant.entity.OrderDetail;
import com.xmut.tearestaurant.entity.Orders;
import lombok.Data;

import java.util.List;

/**
 * @author Bless_Wu
 * @Description
 * @create 2022-12-14 16:02
 */
@Data
public class OrdersDto extends Orders {
    private List<OrderDetail> ordersList;
}
