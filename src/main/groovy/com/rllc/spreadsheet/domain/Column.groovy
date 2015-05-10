package com.rllc.spreadsheet.domain

/**
 * Created by Steven McAdams on 4/27/15.
 */
enum Column {
    MINISTER(value: 'minister'),
    BIBLE_TEXT(value: 'bibletext'),
    DATE(value: 'date'),
    TIME(value: 'time'),
    NOTES(value: 'notes'),
    FILE_LOCATION(value: 'filelocation')

    String value
}