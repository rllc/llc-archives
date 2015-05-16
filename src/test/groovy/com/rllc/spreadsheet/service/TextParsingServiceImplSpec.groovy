package com.rllc.spreadsheet.service

import spock.lang.Specification

/**
 * Created by Steven McAdams on 4/25/15.
 */
class TextParsingServiceImplSpec extends Specification {
    TextParsingService textParsingService

    def mp3Directory = "C:\\example\\archives\\rockford"
    def mp3File = "2015\\20150405_CKumpula.mp3"

    void setup() {
        textParsingService = new TextParsingServiceImpl()
        textParsingService.mp3Directory = mp3Directory
    }

    def "ParseFilename"() {
        when: "filelocation is parsed"
        def filename = textParsingService.parseFilename("$mp3Directory\\$mp3File")

        then: "base mp3 directory is stripped"
        filename == '2015/20150405_CKumpula.mp3'
    }

    def "ParseMinister"() {

        when: "minister is parsed"
        def minister = textParsingService.parseMinister("CRAIG kuMPula")

        then: "minister is cased appropriately"
        minister == "Craig Kumpula"
    }

    def "ParseBibleText"() {
        when: "bible text is parsed"
        def bibleText = textParsingService.parseBibleText("Acts 2:1-12")

        then: "bible text is returned"
        bibleText == "Acts 2:1-12"
    }

    def "ParseTime"() {
        when: "date + time is empty"
        def time = textParsingService.parseTime("")

        then: "no time is parsed"
        time == ""

        when: "time is empty"
        time = textParsingService.parseTime("01/01/2014")

        then: "no time is parsed"
        time == ""

        when: "time is dd/mm/yyyy HH:mm"
        time = textParsingService.parseTime("01/01/2014 10:30")

        then: "time is parsed"
        time == "10:30"

        when: "time is dd/mm/yyyy HH:mm am"
        time = textParsingService.parseTime("01/01/2014 10:30 am")

        then: "time is parsed"
        time == "10:30"

        when: "time is dd/mm/yyyy HH:mm pm"
        time = textParsingService.parseTime("01/01/2014 10:30 pm")

        then: "time is parsed and adjusted"
        time == "22:30"
    }

    def "ParseDate"() {
        def filename = '20150405_CKumpula.mp3'

        when: "date is empty, filelocation is empty"
        def date = textParsingService.parseDate('', '')

        then: "no date is parsed"
        date == ''

        when: "date is empty, filelocation in non standard format"
        date = textParsingService.parseDate('', 'CKumpula_20150405')

        then: "date is empty, no exception thrown"
        date == ''
        noExceptionThrown()

        when: 'date is empty, filelocation in standard format'
        date = textParsingService.parseDate('', filename)

        then: 'date is populated from filelocation'
        date == '04/05/2015'

        when: 'date is invalid, filelocation in standard format'
        date = textParsingService.parseDate('04/2015', filename)

        then: 'date is populated from filelocation'
        date == '04/05/2015'

        when: 'date is valid, filelocation in standard format'
        date = textParsingService.parseDate('04/04/2015', filename)

        then: 'date is populated from metadata'
        date == '04/04/2015'

    }

    def "FormatDate"() {
        when: "format date is called"
        def date = textParsingService.formatDate(12, 12, 2014)

        then: "date is formatted as 'MM/dd/yyyy'"
        date == "12/12/2014"

    }

    def "parseNotes"() {
        when: 'notes is null'
        def notes = textParsingService.parseNotes(null);

        then: 'no notes are parsed'
        notes == '';

        when: 'notes is empty'
        notes = textParsingService.parseNotes('');

        then: 'no notes are parsed'
        notes == '';

        when: 'notes exists'
        notes = textParsingService.parseNotes('Christmas Eve Service')

        then: 'notes are parsed'
        notes == 'Christmas Eve Service'
    }
}
