package org.llc.archive.service

import org.llc.archive.rest.domain.Minister
import org.llc.archive.rest.repository.MinisterRepository
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by Steven McAdams on 4/25/15.
 */
class TextParsingServiceImplSpec extends Specification {

    def ministerRepository = Mock(MinisterRepository)
    TextParsingService textParsingService

    void setup() {
        textParsingService = new TextParsingServiceImpl(
                ministerRepository: ministerRepository
        )
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

        ministerRepository.findAll() >> { v ->
            return ministers.collect { m ->
                def tokens = m.split()
                new Minister(firstName: tokens[0], lastName: tokens[1])
            }
        }

        when: "minister is parsed"
        def minister = textParsingService.parseMinister(input)

        then: "minister is spelled correctly"
        minister == output

        where:
        input            || output
        'CRAIG kuMPula'  || 'Craig Kumpula'
        'Rd Niklua'      || 'Rod Nikula'
        'Joko Hapsorry'  || 'Jouko Haapsaari'
        'Antti Paananen' || 'Antti Paananen'

    }

    @Unroll
    def "Parse Minister From Filename #input"() {
        given:
        def ministers = []
        ministers.add("Craig Kumpula")
        ministers.add("Jouko Haapsaari")
        ministers.add("Nathan Muhonen")
        ministerRepository.findAll() >> { v ->
            return ministers.collect { m ->
                def tokens = m.split()
                new Minister(firstName: tokens[0], lastName: tokens[1])
            }
        }

        ministerRepository.findByLastName(_) >> { v ->
            return ministers.findAll { m ->
                m.split()[1] == v
            }.collect { m ->
                def tokens = m.split()
                new Minister(firstName: tokens[0], lastName: tokens[1])
            }
        }

        when: "filename is not parsed"
        def minister = textParsingService.parseMinisterFromFilename(input)

        then: "minister name is returned"
        minister == output

        where:
        input                     || output
        '20150621-JHaapsaari.mp3' || null
        '20150621_JHaapsaari.mp3' || 'Jouko Haapsaari'
        '20130321_CKumpala.mp3'   || 'Craig Kumpula'
        '20130321_NMuonen.mp3'    || 'Nathan Muhonen'
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
