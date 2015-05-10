package com.rllc.spreadsheet.dao

import com.google.gdata.client.spreadsheet.SpreadsheetService
import com.google.gdata.data.spreadsheet.ListFeed
import com.google.gdata.data.spreadsheet.SpreadsheetEntry
import com.google.gdata.data.spreadsheet.SpreadsheetFeed
import com.google.gdata.data.spreadsheet.WorksheetEntry
import com.google.gdata.data.spreadsheet.WorksheetFeed
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value

import javax.annotation.PostConstruct

/**
 * Created by Robert on 5/9/2015.
 */
abstract class AbstractGoogleSpreadsheetDAO {
    private static final Logger logger = LoggerFactory.getLogger(AbstractGoogleSpreadsheetDAO.class)

    @Value("\${google.username}")
    String username
    @Value("\${google.password}")
    String password

    SpreadsheetService spreadsheetService
    URL listFeedUrl
    ListFeed listFeed

    public abstract String getGoogleSpreadsheet()

    @PostConstruct
    init() {
        spreadsheetService = new SpreadsheetService("Application-v1")
        spreadsheetService.setUserCredentials(username, password)
        URL spreadsheetFeedUrl = new URL(
                "https://spreadsheets.google.com/feeds/spreadsheets/private/full")
        SpreadsheetFeed feed = spreadsheetService.getFeed(spreadsheetFeedUrl,
                SpreadsheetFeed.class)
        List<SpreadsheetEntry> spreadsheets = feed.entries
        if (spreadsheets.size() == 0) {
            logger.info(getGoogleSpreadsheet() + " spreadsheet not found, exiting.")
            System.exit(1)
        }

        SpreadsheetEntry spreadsheetEntry
        for (SpreadsheetEntry spreadsheet : spreadsheets) {
            if (getGoogleSpreadsheet().equals(spreadsheet.title.plainText)) {
                spreadsheetEntry = spreadsheet
            }
        }

        if (spreadsheetEntry == null) {
            logger.info(getGoogleSpreadsheet() + " spreadsheet not found, exiting.")
            System.exit(1)
        }

        WorksheetFeed worksheetFeed = spreadsheetService.getFeed(
                spreadsheetEntry.getWorksheetFeedUrl(), WorksheetFeed.class)
        List<WorksheetEntry> worksheets = worksheetFeed.entries
        // TODO : look up worksheet by configurable name
        WorksheetEntry worksheet = worksheets.get(0)

        listFeedUrl = worksheet.listFeedUrl
        listFeed = spreadsheetService.getFeed(listFeedUrl, ListFeed.class)
    }

    def tryCatch = { Closure operation, Closure defHandler = { null } ->
        try {
            operation()
        } catch (e) {
            defHandler(e)
        }
    }
}
