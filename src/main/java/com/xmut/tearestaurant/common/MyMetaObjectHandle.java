package com.xmut.tearestaurant.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * @author Bless_Wu
 * @Description 元数据处理器,
 * 话接上回，在此类继承mp的MetaObjectHandler，实现具体的insert or update操作自动填充功能
 * @create 2022-10-23 20:39
 */
@Component
@Slf4j
public class MyMetaObjectHandle implements MetaObjectHandler {

    /**
     * 插入的时候自动填充
     * 会有一个问题，就是mp提供的这个类不支持session 所以这边不能通过session获取到当前用户
     * 解决办法 使用threadLocal来解决 在过滤器中的时候就设置了threadlocal
     * 也就是客户端的每一个http都会创建一个thread，这个线程是唯一的
     * threaLocal为每个线程提供一个单独的存储空间，具有线程隔离的效果，只有在线程内才会获取到相应的值，
     * 线程外不能访问
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info(metaObject.toString());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        /**
         * BaseContext工具类 用于保存threadLocal和获取ThreadLocal
         * 在过滤器55行获取当前登录的 用户id保存到threadLocal中 然后再这边就可以直接获取
         * 这样 就不用每次自己手动填写了
         *
         */
        metaObject.setValue("createUser", BaseContext.getCurrentId());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
    }

    /**
     * 修改的时候自动填充
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info(metaObject.toString());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
    }
}
