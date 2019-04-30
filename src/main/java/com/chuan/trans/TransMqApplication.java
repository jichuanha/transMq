package com.chuan.trans;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author jc
 * Date:2019/4/30
 * Time:14:23
 */
@SpringBootApplication(scanBasePackages = {"com.chuan.trans"})
public class TransMqApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransMqApplication.class, args);
    }
}
