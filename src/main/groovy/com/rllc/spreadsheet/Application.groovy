package com.rllc.spreadsheet

import com.google.gdata.util.ServiceException
import com.rllc.spreadsheet.service.ArchivedSermonsService
import com.rllc.spreadsheet.service.ArchivedSermonsServiceImpl
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ApplicationContext

@SpringBootApplication
public class Application {

    public static void main(String[] args)
            throws IOException, ServiceException {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
        ArchivedSermonsService spreadsheetService = ctx.getBean(ArchivedSermonsServiceImpl.class);
        spreadsheetService.updateSpreadsheet();
    }
}
