package com.rllc.spreadsheet

import com.google.gdata.util.ServiceException
import com.rllc.spreadsheet.service.SpreadsheetService
import com.rllc.spreadsheet.service.SpreadsheetServiceImpl
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ApplicationContext

@SpringBootApplication
public class Application {

    public static void main(String[] args)
            throws IOException, ServiceException {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
        SpreadsheetService spreadsheetService = ctx.getBean(SpreadsheetServiceImpl.class);
        spreadsheetService.updateSpreadsheet();
    }
}
