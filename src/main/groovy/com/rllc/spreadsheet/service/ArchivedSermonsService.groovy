package com.rllc.spreadsheet.service

import com.google.gdata.util.ServiceException

/**
 * Created by Steven McAdams on 4/25/15.
 */
interface ArchivedSermonsService {
    def updateSpreadsheet() throws IOException, ServiceException;
}