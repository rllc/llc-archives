package com.rllc.spreadsheet.service

import com.rllc.spreadsheet.domain.RemoteFiles

/**
 * Created by Steven McAdams on 5/25/15.
 */
interface AmazonService {

    RemoteFiles downloadMetadata(List<String> fileNames, String congregationKey)

    List<String> listFiles(boolean refreshAll, String congregationKey)

}