package com.xmut.tearestaurant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmut.tearestaurant.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Bless_Wu
 * @Description
 * @create 2022-11-02 16:41
 */
@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
