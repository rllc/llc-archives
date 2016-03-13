package org.llc.archive.service
/**
 * Created by Steven McAdams on 4/25/15.
 */
interface ArchivedSermonsService {

    def updateDatastore(Date fromDate, Date toDate, List<String> congregations)

}