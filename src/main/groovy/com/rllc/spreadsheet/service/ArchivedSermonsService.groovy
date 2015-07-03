package com.rllc.spreadsheet.service

import com.rllc.spreadsheet.domain.Mp3SermonFile

/**
 * Created by Steven McAdams on 4/25/15.
 */
interface ArchivedSermonsService {
    def updateDatastore()

    def updateDatastore(String name, List<Mp3SermonFile> sermons)
}