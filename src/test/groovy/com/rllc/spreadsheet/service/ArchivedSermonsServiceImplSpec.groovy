package com.rllc.spreadsheet.service

import com.rllc.spreadsheet.dao.SermonDAO
import com.rllc.spreadsheet.domain.Sermon
import spock.lang.Specification

/**
 * Created by Steven McAdams on 4/27/15.
 */
class ArchivedSermonsServiceImplSpec extends Specification {

    def sermonDAO = Mock(SermonDAO)
    ArchivedSermonsService archivedSermonsService

    def setup() {
        archivedSermonsService = new ArchivedSermonsServiceImpl()
        archivedSermonsService.sermonDAO = sermonDAO
    }

    def "UpdateSpreadsheet"() {
        given:
        def storedSermons = []
        def mp3Sermons = []

        storedSermons << new Sermon(
                date: '01/01/2014',
                notes: 'notes',
                filelocation: '20140101_TestCase.mp3'
        )
        mp3Sermons << new Sermon(
                date: '01/01/2014',
                time: '10:30',
                minister: 'test case',
                filelocation: '20140101_TestCase.mp3'
        )
        mp3Sermons << new Sermon(
                date: '01/02/2014',
                time: '10:30',
                minister: 'test case2',
                bibletext: 'bibletext',
                notes: 'notes',
                filelocation: '20140102_TestCase.mp3'
        )
        sermonDAO.get('20140101_TestCase.mp3') >> { v -> return storedSermons[0] }
        sermonDAO.get('20140102_TestCase.mp3') >> { v -> return }

        when: "update spreadsheet is called with updated items"
        archivedSermonsService.updateSpreadsheet(mp3Sermons)

        then: "existing spreadsheet items are updated, new items are created"
        1 * sermonDAO.update(_ as Sermon) >> { Sermon sermon ->
            assert sermon.bibletext == ''
            assert sermon.date == '01/01/2014'
            assert sermon.time == '10:30'
            assert sermon.notes == 'notes'
            assert sermon.minister == 'test case'
            assert sermon.filelocation == '20140101_TestCase.mp3'
        }
        1 * sermonDAO.create(_ as Sermon) >> { Sermon sermon ->
            assert sermon.bibletext == 'bibletext'
            assert sermon.date == '01/02/2014'
            assert sermon.time == '10:30'
            assert sermon.notes == 'notes'
            assert sermon.minister == 'test case2'
            assert sermon.filelocation == '20140102_TestCase.mp3'
        }

    }
}
