package com.rllc.spreadsheet

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
//        ApplicationContext ctx = SpringApplication.run(Application.class, args);
//        ctx.getBean(Bootstrap.class).execute()
    }
}
