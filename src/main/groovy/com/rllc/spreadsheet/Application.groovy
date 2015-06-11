package com.rllc.spreadsheet

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ApplicationContext

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
//        ArchivedSermonsService spreadsheetService = ctx.getBean(ArchivedSermonsServiceImpl.class);
//        spreadsheetService.update();

//        ArchivedSermonsService spreadsheetService = ctx.getBean(ArchivedSermonsServiceImpl.class);
//        spreadsheetService.update();
    }
}
