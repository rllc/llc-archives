package org.llc.archive.service

import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by Steven McAdams on 4/25/15.
 */
class TextParsingServiceImplSpec extends Specification {

    TextParsingService textParsingService

    void setup() {
        textParsingService = new TextParsingServiceImpl()
    }

    @Unroll
    def "parse filename #mp3File"() {
        when: "filelocation is parsed"
        def filename = textParsingService.parseFilename(mp3Directory, mp3File)

        then: "base mp3 directory is stripped"
        filename == parsedFilename

        where:
        mp3Directory                      | mp3File                       || parsedFilename
        'C:\\example\\archives\\rockford' | '2015\\20150405_CKumpula.mp3' || '2015/20150405_CKumpula.mp3'

    }

    @Unroll
    def "Parse Minister #input"() {
        given:

        when: "minister is parsed"
        def parsedMinister = textParsingService.parseMinister(input)

        then: "minister is spelled correctly"
        parsedMinister == ministerName

        where:
        input                 || ministerName
        null                  || ''
        ''                    || ''
        'first last'          || 'First Last'
        ' first last '        || 'First Last'
        ' first middle last ' || 'First Middle Last'
    }

    @Unroll
    def "parse bible text #input"() {
        when: "bible text is parsed"
        def bibleText = textParsingService.parseBibleText(input)

        then: "bible text is returned"
        bibleText == output

        where:
        input         || output
        null          || ''
        ''            || ''
        'Acts 2:1-12' || 'Acts 2:1-12'

    }

    @Unroll
    def "Parse Time #input"() {
        when: "date + time parsed"
        def time = textParsingService.parseTime(input)

        then: "time is returned"
        time == output

        where:
        input                 || output
        ''                    || ''
        '01/01/2014'          || ''
        '01/01/2014 10:30'    || '10:30'
        '01/01/2014 10:30 am' || '10:30'
        '01/01/2014 10:30 pm' || '22:30'

    }

    @Unroll
    def "parse date (#inputDate, #fileLocation)"() {
        when: "date is parsed"
        def date = textParsingService.parseDate(inputDate, fileLocation)

        then: "date is returned"
        date == parsedDate

        where:
        inputDate           | fileLocation            || parsedDate
        ''                  | ''                      || ''
        ''                  | 'CKumpula_20150405'     || ''
        ''                  | '20150405_CKumpula.mp3' || '04/05/2015'
        '04/2015'           | '20150405_CKumpula.mp3' || '04/05/2015'
        '04/04/2015'        | '20150405_CKumpula.mp3' || '04/04/2015'
        ' 07/26/2015 10:30' | '20150405_CKumpula.mp3' || '07/26/2015'
        '3/16/14 2 pm'      | '20150405_CKumpula.mp3' || '03/16/2014'

    }

    @Unroll
    def "Format Date (#day, #month, #year)"() {
        when: "format date is called"
        def date = textParsingService.formatDate(day, month, year)

        then: "date is formatted as 'MM/dd/yyyy'"
        date == formattedDate

        where:
        day | month | year || formattedDate
        12  | 12    | 2014 || '12/12/2014'

    }

    @Unroll
    def "Parse Notes #input"() {
        when: 'notes are parsed'
        def notes = textParsingService.parseNotes(input);

        then: 'comments are returned'
        notes == output;

        where:
        input                   || output
        null                    || ''
        ''                      || ''
        'Christmas Eve Service' || 'Christmas Eve Service'

    }
}
