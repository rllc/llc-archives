package com.rllc.spreadsheet.service

import com.rllc.spreadsheet.domain.RemoteFiles
import com.rllc.spreadsheet.rest.repository.MinisterRepository
import org.apache.commons.io.FileUtils
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

/**
 * Created by Steven McAdams on 4/26/15.
 */
class RemoteMp3DiscoveryServiceImplSpec extends Specification {

    private static final Logger logger = LoggerFactory.getLogger(RemoteMp3DiscoveryServiceImplSpec.class);

    AmazonService amazonService = Mock(AmazonService.class)

    TextParsingService textParsingService
    Mp3DiscoveryService mp3DiscoveryService
    MinisterRepository ministerRepository = Mock(MinisterRepository)

    @Rule
    public TemporaryFolder mp3Directory = new TemporaryFolder();

    def sermonFiles = [
            '/sermons/rockford/2014/20141224_NMuhonen.mp3',
            '/sermons/rockford/2015/20150315_JBloomquist.mp3',
            '/sermons/rockford/2015/20150315_RNevala.mp3'
    ]

    void setup() {
        textParsingService = new TextParsingServiceImpl(
                ministerRepository: ministerRepository
        )
        initializeFiles()
        mp3DiscoveryService = setupMp3DiscoveryService()
    }


    def setupMp3DiscoveryService() {
        amazonService.listFiles(_, _) >> { v -> return sermonFiles }
        amazonService.downloadMetadata(_, _) >> { v ->
            new RemoteFiles(
                    root: mp3Directory.root.absolutePath,
                    files: sermonFiles.collect {
                        new File("${mp3Directory.root}${it}")
                    })
        }

        new RemoteMp3DiscoveryServiceImpl(
                amazonService: amazonService,
                textParsingService: textParsingService,
                fileFilteringService: Mock(FileFilteringService),
                databaseCleanupService: Mock(DatabaseCleanupService)
        )
    }

    def initializeFiles() {
        sermonFiles.each { filePath ->
            URL inputUrl = getClass().getResource(filePath);
            File dest = new File("${mp3Directory.root}${filePath}");
            FileUtils.copyURLToFile(inputUrl, dest);
        }
    }


    def "process mp3 files"() {
        when: "mp3 files are found and processed"
        def sermons = mp3DiscoveryService.processMp3Files(false, 'rllc')

        then: "sermons are parsed from files"
        sermons.each { sermon ->
            switch (sermon.minister) {
                case "Nathan Muhonen":
                    assert sermon.bibletext == "Luke 2:1-20"
                    assert sermon.date == "12/24/2012"
                    assert sermon.comments == "Christmas Eve Service"
                    assert sermon.file.contains('sermons/rockford/2014/20141224_NMuhonen.mp3')
                    break;
                case "Jon Bloomquist":
                    assert sermon.bibletext == "Deuteronomy 5:1-4"
                    assert sermon.date == "03/15/2015"
                    assert sermon.comments == ""
                    assert sermon.file.contains('sermons/rockford/2015/20150315_JBloomquist.mp3')
                    break;
                case "Rick Nevala":
                    assert sermon.bibletext == "Acts 8:9-20"
                    assert sermon.date == "03/15/2015"
                    assert sermon.comments == ""
                    assert sermon.file.contains('sermons/rockford/2015/20150315_RNevala.mp3')
                    break;
            }
        }
    }

}
