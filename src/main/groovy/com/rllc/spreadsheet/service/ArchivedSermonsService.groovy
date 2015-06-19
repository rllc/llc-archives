package com.rllc.spreadsheet.service

import com.rllc.spreadsheet.rest.domain.Sermon


/**
 * Created by Steven McAdams on 4/25/15.
 */
interface ArchivedSermonsService {
    def updateDatastore()

    def updateDatastore(List<Sermon> sermons)
}