package com.rllc.spreadsheet.service

import com.rllc.spreadsheet.domain.Sermon

/**
 * Created by Steven McAdams on 4/25/15.
 */
interface ArchivedSermonsService {
    def updateSpreadsheet()

    def updateSpreadsheet(List<Sermon> sermons)
}