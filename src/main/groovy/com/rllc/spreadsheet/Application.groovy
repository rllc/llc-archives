package com.rllc.spreadsheet

import com.rllc.spreadsheet.config.Bootstrap
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ApplicationContext

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
        ctx.getBean(Bootstrap.class).execute()
    }
}
