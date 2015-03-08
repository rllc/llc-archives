package com.rllc.spreadsheet;

import com.google.gdata.util.ServiceException;
import com.rllc.spreadsheet.service.SpreadsheetService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class Application {

    public static void main(String[] args)
            throws IOException, ServiceException {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
        SpreadsheetService spreadsheetService = ctx.getBean(SpreadsheetService.class);
        spreadsheetService.updateSpreadsheet();
    }
}
