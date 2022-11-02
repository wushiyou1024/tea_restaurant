package com.xmut.tearestaurant.common;

/**
 * @author Bless_Wu
 * @Description 自定义业务异常
 * @create 2022-10-24 21:03
 */
public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}
