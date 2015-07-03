package com.rllc.spreadsheet.service
/**
 * Created by Steven McAdams on 5/25/15.
 */
interface AmazonService {

    def downloadMetadata(List<String> fileNames, String congregationKey)

    List<String> listFiles(String congregationKey)

}