package com.rllc.spreadsheet.service

/**
 * Created by Steven McAdams on 4/25/15.
 */
interface TextParsingService {

    String parseFilename(String absoluteFilePath)
    String parseMinister(String artist)
    String parseBibleText(String album)
    String parseTime(String title)
    String parseDate(String title, String name)
    String formatDate(int month, int day, int year)

}