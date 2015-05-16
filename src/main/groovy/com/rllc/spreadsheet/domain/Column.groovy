package com.rllc.spreadsheet.domain

/**
 * Created by Steven McAdams on 4/27/15.
 */
enum Column {
    MINISTER(label: 'minister'),
    BIBLE_TEXT(label: 'bibletext'),
    DATE(label: 'date'),
    TIME(label: 'time'),
    NOTES(label: 'notes'),
    FILE_LOCATION(label: 'filelocation')

    String label
}