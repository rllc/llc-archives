package org.llc.archive.service

import org.llc.archive.domain.Mp3SermonFile

/**
 * Created by Steven McAdams on 4/25/15.
 */
interface ArchivedSermonsService {

    def updateDatastore(Date fromDate, Date toDate, List<String> congregations)

    def updateDatastore(String name, List<Mp3SermonFile> sermons)
}