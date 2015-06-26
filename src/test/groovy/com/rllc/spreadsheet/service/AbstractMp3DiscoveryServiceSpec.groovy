package com.rllc.spreadsheet.service

import com.rllc.spreadsheet.rest.repository.MinisterCrudRepository
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

/**
 * Created by Steven McAdams on 5/10/15.
 */
abstract class AbstractMp3DiscoveryServiceSpec extends Specification {

    private static final Logger logger = LoggerFactory.getLogger(AbstractMp3DiscoveryServiceSpec.class);

    TextParsingService textParsingService
    Mp3DiscoveryService mp3DiscoveryService
    def ministerCrudRepository = Mock(MinisterCrudRepository)

    @Rule
    public TemporaryFolder mp3Directory = new TemporaryFolder();

    def sermonFiles = [
            '/sermons/rockford/2014/20141224_NMuhonen.mp3',
            '/sermons/rockford/2015/20150315_JBloomquist.mp3',
            '/sermons/rockford/2015/20150315_RNevala.mp3'
    ]

    void setup() {
        textParsingService = new TextParsingServiceImpl(
                ministerCrudRepository: ministerCrudRepository
        )
        initializeFiles()
        mp3DiscoveryService = setupMp3DiscoveryService()
    }

    abstract setupMp3DiscoveryService()

    abstract initializeFiles()


    def "GetMp3s"() {
        when: "mp3 files are found and processed"
        def sermons = mp3DiscoveryService.processMp3Files('rllc')

        then: "sermons are parsed from files"
        sermons.each { sermon ->
            switch (sermon.minister) {
                case "Nathan Muhonen":
                    assert sermon.bibletext == "Luke 2:1-20"
                    assert sermon.date == "12/24/2012"
                    assert sermon.notes == "Christmas Eve Service"
                    assert sermon.file.contains('sermons/rockford/2014/20141224_NMuhonen.mp3')
                    break;
                case "Jon Bloomquist":
                    assert sermon.bibletext == "Deuteronomy 5:1-4"
                    assert sermon.date == "03/15/2015"
                    assert sermon.notes == ""
                    assert sermon.file.contains('sermons/rockford/2015/20150315_JBloomquist.mp3')
                    break;
                case "Rick Nevala":
                    assert sermon.bibletext == "Acts 8:9-20"
                    assert sermon.date == "03/15/2015"
                    assert sermon.notes == ""
                    assert sermon.file.contains('sermons/rockford/2015/20150315_RNevala.mp3')
                    break;
            }
        }
    }

    def "FindMp3Files"() {
        when: "mp3 directory is scanned for files"
        def files = mp3DiscoveryService.findMp3Files('rllc')

        then: "all sermons are found"
        sermonFiles.each { sermon ->
            logger.info "> verifying $sermon was found.."
            def found = false
            files.each { mp3File ->
                if (mp3File.absolutePath.contains(sermon)) {
                    found = true
                }
            }
            assert found == true
        }
    }

}
