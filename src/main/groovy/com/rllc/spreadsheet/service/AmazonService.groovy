package com.rllc.spreadsheet.service

import com.rllc.spreadsheet.domain.AmazonCredentials

/**
 * Created by Steven McAdams on 5/25/15.
 */
interface AmazonService {

    List<File> downloadMetadata(List<String> fileNames, AmazonCredentials amazonCredentials)

    List<String> listFiles(AmazonCredentials amazonCredentials)

}