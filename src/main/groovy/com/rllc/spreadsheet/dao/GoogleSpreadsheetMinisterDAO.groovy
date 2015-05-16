package com.rllc.spreadsheet.dao

import com.google.gdata.client.spreadsheet.SpreadsheetService
import com.google.gdata.data.spreadsheet.ListEntry
import com.google.gdata.data.spreadsheet.ListFeed
import com.rllc.spreadsheet.domain.Minister
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

import static com.rllc.spreadsheet.domain.Column.MINISTER

/**
 * Created by Robert on 5/9/15.
 */

@Component
class GoogleSpreadsheetMinisterDAO extends AbstractGoogleSpreadsheetDAO implements MinisterDAO {

    private static final Logger logger = LoggerFactory.getLogger(GoogleSpreadsheetMinisterDAO.class)

    @Value("\${google.ministerSpreadsheet}")
    String googleSpreadsheet

    SpreadsheetService spreadsheetService
    URL listFeedUrl
    ListFeed listFeed

    @Override
    List<Minister> getMinisters() {
        listFeed.entries.collect { row ->
            new Minister(
                    name: row.customElements.getValue(MINISTER.value)
            )
        }
    }

    @Override
    boolean ministerExists(String name) {
        def minister = listFeed.entries.find { row ->
            row.customElements.getValue(MINISTER.value).equals(name)
        }
        return minister != null
    }

    @Override
    void create(Minister minister) {
        logger.info("Creating a new entry in spreadsheet for [${minister}]")

        ListEntry row = new ListEntry()
        row.customElements.setValueLocal(MINISTER.value, minister.name)

        tryCatch({ spreadsheetService.insert(listFeedUrl, row) }, { e -> e.printStackTrace() })
    }

    @Override
    void delete(Minister minister) {
        for (ListEntry row : listFeed.entries) {
            if (row.customElements.getValue(MINISTER.value).contains(minister.name)) {
                tryCatch({ row.delete() }, { e -> e.printStackTrace() })
            }
        }
    }
}
