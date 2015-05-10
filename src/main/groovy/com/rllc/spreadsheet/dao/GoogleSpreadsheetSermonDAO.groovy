package com.rllc.spreadsheet.dao

import com.google.gdata.data.spreadsheet.*
import com.rllc.spreadsheet.domain.Sermon
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/**
 * Created by Steven McAdams on 4/26/15.
 */

@Component
class GoogleSpreadsheetSermonDAO extends AbstractGoogleSpreadsheetDAO implements SermonDAO  {

    private static final Logger logger = LoggerFactory.getLogger(GoogleSpreadsheetSermonDAO.class)

    private static final String MINISTER = "minister"
    private static final String BIBLE_TEXT = "bibletext"
    private static final String DATE = "date"
    private static final String TIME = "time"
    private static final String NOTES = "notes"
    private static final String FILE_LOCATION = "filelocation"

    @Value("\${google.spreadsheet}")
    String googleSpreadsheet
    @Value("\${aws.bucket}")
    String awsBucket

    public String getGoogleSpreadsheet() {
        return this.googleSpreadsheet
    }

    @Override
    Sermon get(String filename) {
        for (ListEntry row : listFeed.entries) {
            if (row.customElements.getValue(FILE_LOCATION).contains(filename)) {
                return new Sermon(
                        minister: row.customElements.getValue(MINISTER),
                        bibletext: row.customElements.getValue(BIBLE_TEXT),
                        date: row.customElements.getValue(DATE),
                        time: row.customElements.getValue(TIME),
                        notes: row.customElements.getValue(NOTES),
                        filelocation: filename
                )
            }
        }
    }

    @Override
    void create(Sermon sermon) {
        logger.info("Creating a new entry in spreadsheet for [${sermon.filelocation}]")

        ListEntry row = new ListEntry()
        setRowValues(row, sermon)

        tryCatch({ spreadsheetService.insert(listFeedUrl, row) }, { e -> e.printStackTrace() })
    }

    @Override
    void update(Sermon sermon) {
        for (ListEntry row : listFeed.entries) {
            if (row.customElements.getValue(FILE_LOCATION).contains(sermon.filelocation)) {
                setRowValues(row, sermon)
                tryCatch({ row.update() }, { e -> e.printStackTrace() })
            }
        }
    }

    @Override
    void delete(Sermon sermon) {
        for (ListEntry row : listFeed.entries) {
            if (row.customElements.getValue(FILE_LOCATION).contains(sermon.filelocation)) {
                tryCatch({ row.delete() }, { e -> e.printStackTrace() })
            }
        }
    }

    private void setRowValues(ListEntry row, Sermon sermon) {
        row.customElements.setValueLocal(MINISTER, sermon.minister)
        row.customElements.setValueLocal(BIBLE_TEXT, sermon.bibletext)
        row.customElements.setValueLocal(DATE, sermon.date)
        row.customElements.setValueLocal(TIME, sermon.time)
        row.customElements.setValueLocal(NOTES, sermon.notes)
        row.customElements.setValueLocal(FILE_LOCATION, awsBucket + sermon.getFilelocation())
    }
}
