package com.xmut.tearestaurant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Bless_Wu
 * @Description
 * @create 2022-10-20 21:21
 */

@Slf4j
@ServletComponentScan
@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
public class TeaResApplication {
    public static void main(String[] args) {
        SpringApplication.run(TeaResApplication.class, args);
        log.info("项目启动成");
    }
}
