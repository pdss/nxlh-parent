package com.nxlh.manager;


import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.concurrent.CountDownLatch;


@MapperScan(value = "com.nxlh.manager.mapper")
@ComponentScan("com.nxlh")
@SpringBootApplication
@EnableCaching
public class ManagerServiceApplicationStarter {

    public static void main(String[] args) throws InterruptedException {
        new SpringApplicationBuilder()
                .sources(ManagerServiceApplicationStarter.class)
                .web(WebApplicationType.NONE)
                .run(args);

//        CountDownLatch countDownLatch = new CountDownLatch(1);
////        countDownLatch.await();
    }
}