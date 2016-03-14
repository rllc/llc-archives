package org.llc.archive.service

import org.apache.commons.io.FileUtils
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import org.llc.archive.domain.RemoteFile
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by Steven McAdams on 4/26/15.
 */
class RemoteMp3DiscoveryServiceImplSpec extends Specification {

    private static final Logger logger = LoggerFactory.getLogger(RemoteMp3DiscoveryServiceImplSpec.class);

    AmazonService amazonService = Mock(AmazonService.class)

    TextParsingService textParsingService
    RemoteMp3DiscoveryServiceImpl mp3DiscoveryService

    @Rule
    public TemporaryFolder mp3Directory = new TemporaryFolder();

    def sermonFiles = [
            '/sermons/rockford/2014/20141224_NMuhonen.mp3',
            '/sermons/rockford/2015/20150315_JBloomquist.mp3',
            '/sermons/rockford/2015/20150315_RNevala.mp3'
    ]

    def initializeFiles() {
        sermonFiles.each { filePath ->
            URL inputUrl = getClass().getResource(filePath);
            File dest = new File("${mp3Directory.root}${filePath}");
            FileUtils.copyURLToFile(inputUrl, dest);
        }
    }

    void setup() {
        textParsingService = new TextParsingServiceImpl()
        initializeFiles()
        mp3DiscoveryService = setupMp3DiscoveryService()
    }

    def setupMp3DiscoveryService() {
        amazonService.listFiles(_, _) >> { v -> return sermonFiles }
        amazonService.downloadMetadata(_, _) >> { fileName, congregationKey ->
            new RemoteFile(
                    root: mp3Directory.root.absolutePath,
                    file: new File("${mp3Directory.root}${fileName}")
            )
        }

        new RemoteMp3DiscoveryServiceImpl(
                amazonService: amazonService,
                textParsingService: textParsingService,
                fileFilteringService: Mock(FileFilteringService),
                databaseCleanupService: Mock(DatabaseCleanupService)
        )
    }


    @Unroll
    def "process mp3 file #inputFile"() {
        when: "mp3 files are found and processed"
        def sermon = mp3DiscoveryService.extractMp3Data(
                new RemoteFile(
                        file: new File("${mp3Directory.root}${inputFile}"),
                        root: mp3Directory.root
                )
        )

        then: "metadata is parsed from file"
        sermon.minister == minister
        sermon.bibletext == bibleText
        sermon.date == date
        sermon.comments == comments
        sermon.file.contains(fileName)

        where:
        inputFile                                         || minister         || bibleText           || date         || comments                || fileName
        '/sermons/rockford/2014/20141224_NMuhonen.mp3'    || 'Nathan Muhonen' || 'Luke 2:1-20'       || '12/24/2012' || 'Christmas Eve Service' || '20141224_NMuhonen'
        '/sermons/rockford/2015/20150315_JBloomquist.mp3' || 'Jon Bloomquist' || 'Deuteronomy 5:1-4' || '03/15/2015' || ''                      || '20150315_JBloomquist'
        '/sermons/rockford/2015/20150315_RNevala.mp3'     || 'Rick Nevala'    || 'Acts 8:9-20'       || '03/15/2015' || ''                      || '20150315_RNevala'
    }

}
