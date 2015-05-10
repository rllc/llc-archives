package com.rllc.spreadsheet.dao

import com.google.gdata.client.spreadsheet.SpreadsheetService
import com.google.gdata.data.spreadsheet.*
import com.rllc.spreadsheet.domain.Minister
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/**
 * Created by Robert on 5/9/15.
 */

@Component
class GoogleSpreadsheetMinisterDAO extends AbstractGoogleSpreadsheetDAO implements MinisterDAO {

    private static final Logger logger = LoggerFactory.getLogger(GoogleSpreadsheetMinisterDAO.class)


    private static final String MINISTER = "minister"

    @Value("\${google.ministerSpreadsheet}")
    String googleSpreadsheet

    SpreadsheetService spreadsheetService
    URL listFeedUrl
    ListFeed listFeed


    public String getGoogleSpreadsheet() {
        return this.googleSpreadsheet
    }

    @Override
    List<Minister> getMinisters() {
        List<Minister> ministers = new ArrayList<>()
        for (ListEntry row : listFeed.entries) {
            ministers.add(new Minister(
                    name: row.customElements.getValue(MINISTER)
            ));
        }
        return ministers
    }

    @Override
    boolean ministerExists(String name) {
        for (ListEntry row : listFeed.entries) {
            if (row.customElements.getValue(MINISTER).equals(name)) {
                return true
            }
        }
        return false
    }

    @Override
    void create(Minister minister) {
        logger.info("Creating a new entry in spreadsheet for [${minister}]")

        ListEntry row = new ListEntry()
        row.customElements.setValueLocal(MINISTER, minister.name)

        tryCatch({ spreadsheetService.insert(listFeedUrl, row) }, { e -> e.printStackTrace() })
    }

    @Override
    void delete(Minister minister) {
        for (ListEntry row : listFeed.entries) {
            if (row.customElements.getValue(MINISTER).contains(minister.name)) {
                tryCatch({ row.delete() }, { e -> e.printStackTrace() })
            }
        }
    }
}
