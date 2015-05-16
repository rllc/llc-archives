package com.rllc.spreadsheet.dao

import com.google.gdata.data.spreadsheet.*
import com.rllc.spreadsheet.domain.Sermon
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

import static com.rllc.spreadsheet.domain.Column.*

/**
 * Created by Steven McAdams on 4/26/15.
 */

@Component
class GoogleSpreadsheetSermonDAO extends AbstractGoogleSpreadsheetDAO implements SermonDAO  {

    private static final Logger logger = LoggerFactory.getLogger(GoogleSpreadsheetSermonDAO.class)

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
            if (row.customElements.getValue(FILE_LOCATION.label).contains(filename)) {
                return new Sermon(
                        minister: row.customElements.getValue(MINISTER.label),
                        bibletext: row.customElements.getValue(BIBLE_TEXT.label),
                        date: row.customElements.getValue(DATE.label),
                        time: row.customElements.getValue(TIME.label),
                        notes: row.customElements.getValue(NOTES.label),
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
            if (row.customElements.getValue(FILE_LOCATION.label).contains(sermon.filelocation)) {
                setRowValues(row, sermon)
                tryCatch({ row.update() }, { e -> e.printStackTrace() })
            }
        }
    }

    @Override
    void delete(Sermon sermon) {
        for (ListEntry row : listFeed.entries) {
            if (row.customElements.getValue(FILE_LOCATION.label).contains(sermon.filelocation)) {
                tryCatch({ row.delete() }, { e -> e.printStackTrace() })
            }
        }
    }

    private void setRowValues(ListEntry row, Sermon sermon) {
        row.customElements.setValueLocal(MINISTER.label, sermon.minister)
        row.customElements.setValueLocal(BIBLE_TEXT.label, sermon.bibletext)
        row.customElements.setValueLocal(DATE.label, sermon.date)
        row.customElements.setValueLocal(TIME.label, sermon.time)
        row.customElements.setValueLocal(NOTES.label, sermon.notes)
        row.customElements.setValueLocal(FILE_LOCATION.label, awsBucket + sermon.getFilelocation())
    }
}
