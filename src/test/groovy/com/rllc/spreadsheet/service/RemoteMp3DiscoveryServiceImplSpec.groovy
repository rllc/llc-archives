package com.rllc.spreadsheet.service

import com.rllc.spreadsheet.domain.AmazonCredentials
import org.apache.commons.io.FileUtils

/**
 * Created by Steven McAdams on 4/26/15.
 */
class RemoteMp3DiscoveryServiceImplSpec extends AbstractMp3DiscoveryServiceSpec {

    def amazonService = Mock(AmazonService.class)

    @Override
    def setupMp3DiscoveryService() {
        def amazonCredentials = new AmazonCredentials(
                accessKey: 'rllc-key',
                secretKey: 'rllc-secret',
                bucket: 'rllc-archives'
        )

        amazonService.listFiles(_) >> { v -> return sermonFiles }
        amazonService.downloadMetadata(_, _) >> { v ->
            [root: mp3Directory.root.absolutePath, files: sermonFiles.collect {
                new File("${mp3Directory.root}${it}")
            }]
        }

        new RemoteMp3DiscoveryServiceImpl(
                amazonService: amazonService,
                textParsingService: textParsingService
        )
    }

    @Override
    def initializeFiles() {
        sermonFiles.each { filePath ->
            URL inputUrl = getClass().getResource(filePath);
            File dest = new File("${mp3Directory.root}${filePath}");
            FileUtils.copyURLToFile(inputUrl, dest);
        }
    }

}
