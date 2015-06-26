package com.rllc.spreadsheet.service

import com.rllc.spreadsheet.rest.domain.Minister
import com.rllc.spreadsheet.rest.repository.MinisterCrudRepository
import spock.lang.Specification

/**
 * Created by Steven McAdams on 4/25/15.
 */
class TextParsingServiceImplSpec extends Specification {

    def ministerCrudRepository = Mock(MinisterCrudRepository)
    TextParsingService textParsingService

    def mp3File = "2015\\20150405_CKumpula.mp3"

    void setup() {
        textParsingService = new TextParsingServiceImpl(
                ministerCrudRepository: ministerCrudRepository
        )
    }

    def "ParseFilename"() {
        when: "filelocation is parsed"
        def filename = textParsingService.parseFilename(mp3File)

        then: "base mp3 directory is stripped"
        filename == '2015/20150405_CKumpula.mp3'
    }

    def "ParseMinister"() {
        given:
        def ministers = []

        ministers.add("Craig Kumpula")
        ministers.add("Joe Lehtola")
        ministers.add("John Lehtola")
        ministers.add("Jon Bloomquist")
        ministers.add("Jouko Haapsaari")
        ministers.add("Keith Waaraniemi")
        ministers.add("Mark Lee")
        ministers.add("Markus Lohi")
        ministers.add("Nathan Muhonen")
        ministers.add("Randy Haapala")
        ministers.add("Randy Herrala")
        ministers.add("Ray Waaraniemi")
        ministers.add("Rick Nevala")
        ministers.add("Rocky Sorvala")
        ministers.add("Rod Nikula")
        ministers.add("Ron Honga")
        ministers.add("Sam Roiko")

        ministerCrudRepository.findAll() >> { v ->
            return ministers.collect { m ->
                def tokens = m.split()
                new Minister(firstName: tokens[0], lastName: tokens[1])
            }
        }

        when: "minister is parsed"
        def minister = textParsingService.parseMinister("CRAIG kuMPula")

        then: "minister is cased appropriately"
        minister == "Craig Kumpula"

        when: "minister is misspelled"
        minister = textParsingService.parseMinister("Rd Niklua")

        then: "minister is auto-corrected"
        minister == "Rod Nikula"

        when: "minister is misspelled 2"
        minister = textParsingService.parseMinister("Joko Hapsorry")

        then: "minister is auto-corrected"
        minister == "Jouko Haapsaari"

        when: "a new minister is used"
        minister = textParsingService.parseMinister("Antti Paananen")

        then: "minister is not auto-corrected"
        minister == "Antti Paananen"
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
