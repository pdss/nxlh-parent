package com.nxlh.minapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableTransactionManagement
@ComponentScan("com.nxlh")
public class ManagerMinAppApplicationStarter {

    public static void main(String[] args) {
        SpringApplication.run(ManagerMinAppApplicationStarter.class, args);
    }
}
