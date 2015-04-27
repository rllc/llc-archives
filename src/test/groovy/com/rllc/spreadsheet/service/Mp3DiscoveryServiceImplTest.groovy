package com.rllc.spreadsheet.service

import org.apache.commons.io.FileUtils
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

/**
 * Created by z0019yw on 4/26/15.
 */
class Mp3DiscoveryServiceImplTest extends Specification {

    TextParsingService textParsingService
    Mp3DiscoveryService mp3DiscoverService

    def sermons = [
            '/sermons/rockford/2014/20141224_NMuhonen.mp3',
            '/sermons/rockford/2015/20150315_JBloomquist.mp3',
            '/sermons/rockford/2015/20150315_RNevala.mp3'
    ]

    @Rule
    public TemporaryFolder mp3Directory = new TemporaryFolder();

    def setup() {
        sermons.each { filePath ->
            URL inputUrl = getClass().getResource(filePath);
            File dest = new File("${mp3Directory.root}${filePath}");
            FileUtils.copyURLToFile(inputUrl, dest);
        }

        textParsingService = new TextParsingServiceImpl()
        textParsingService.mp3Directory = "${mp3Directory.root}"

        mp3DiscoverService = new Mp3DiscoveryServiceImpl()
        mp3DiscoverService.mp3Directory = "${mp3Directory.root}"
        mp3DiscoverService.textParsingService = textParsingService
    }

    def "GetMp3s"() {
        when: "mp3 files are found and processed"
        def sermons = mp3DiscoverService.getMp3s()

        then: "sermons are parsed from files"
        sermons.each { sermon ->
            switch (sermon.minister) {
                case "Nathan Muhonen":
                    assert sermon.bibletext == "Luke 2:1-20"
                    assert sermon.date == "12/24/2012"
                    assert sermon.time == "10:30"
                    assert sermon.notes == ""
                    assert sermon.filelocation == 'sermons/rockford/2014/20141224_NMuhonen.mp3'
                    break;
                case "Jon Bloomquist":
                    assert sermon.bibletext == "Deuteronomy 5:1-4"
                    assert sermon.date == "03/15/2015"
                    assert sermon.time == "18:30"
                    assert sermon.notes == ""
                    assert sermon.filelocation == 'sermons/rockford/2015/20150315_JBloomquist.mp3'
                    break;
                case "Rick Nevala":
                    assert sermon.bibletext == "Acts 8:9-20"
                    assert sermon.date == "03/15/2015"
                    assert sermon.time == ""
                    assert sermon.notes == ""
                    assert sermon.filelocation == 'sermons/rockford/2015/20150315_RNevala.mp3'
                    break;
            }
        }
    }

    def "FindMp3Files"() {
        when: "mp3 directory is scanned for files"
        def files = mp3DiscoverService.findMp3Files()

        then: "all sermons are found"
        sermons.each { sermon ->
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
