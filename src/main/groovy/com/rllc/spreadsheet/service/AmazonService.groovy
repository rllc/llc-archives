package com.rllc.spreadsheet.service

import com.rllc.spreadsheet.domain.RemoteFiles
import com.rllc.spreadsheet.domain.S3File

/**
 * Created by Steven McAdams on 5/25/15.
 */
interface AmazonService {

    RemoteFiles downloadMetadata(List<String> fileNames, String congregationKey)

    List<S3File> listFiles(String congregationKey)

}