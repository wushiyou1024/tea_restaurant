package com.xmut.tearestaurant.common;

/**
 * @author Bless_Wu
 * @Description 基于threadLocal封装工具类，用与保存与过去当前登录用户id
 * @create 2022-10-23 21:03
 */
public class BaseContext {
    private static  ThreadLocal<Long> threadLocal=new ThreadLocal<>();
    public static void setCurrentId(Long id)
    {
        threadLocal.set(id);
    }
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
