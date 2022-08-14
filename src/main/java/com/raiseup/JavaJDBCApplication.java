package com.raiseup;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class JavaJDBCApplication {
    public static void main(String[] args) {
        SpringApplication.run(JavaJDBCApplication.class,args);
        log.info("Application is up and running...");

    }
}
