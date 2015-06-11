package com.rllc.spreadsheet.dao

import com.rllc.spreadsheet.domain.Sermon
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

/**
 * Created by Steven McAdams on 6/9/15.
 */
@Component
class InMemorySermonDAO implements SermonDAO {

    List<Sermon> sermons = []

    @PostConstruct
    public void init() {

        sermons << new Sermon(
                id: '20140101_TestCase',
                date: '01/01/2014',
                notes: 'notes',
                filelocation: '20140101_TestCase.mp3'
        )
        sermons << new Sermon(
                id: '20140103_TestCase',
                date: '01/01/2014',
                time: '10:30',
                minister: 'test case',
                filelocation: '20140103_TestCase.mp3'
        )
        sermons << new Sermon(
                id: '20140102_TestCase',
                date: '01/02/2014',
                time: '10:30',
                minister: 'test case2',
                bibletext: 'bibletext',
                notes: 'notes',
                filelocation: '20140102_TestCase.mp3'
        )
    }

    @Override
    Sermon get(String filename) {
        return sermons.find { it.filelocation.contains(filename) }
    }

    @Override
    List<Sermon> list() {
        return sermons
    }

    @Override
    void create(Sermon sermon) {
        sermons << sermon
    }

    @Override
    void update(Sermon sermon) {
        def sermonToUpdate = sermons.find { it.filelocation == sermon.filelocation }
        sermonToUpdate = sermon
    }

    @Override
    void delete(Sermon sermon) {
        sermons.remove(sermon.find { it.filelocation == sermon.filelocation })
    }
}
