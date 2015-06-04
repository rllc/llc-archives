package com.rllc.spreadsheet.service

import org.apache.commons.io.FileUtils

/**
 * Created by Steven McAdams on 4/26/15.
 */
class LocalMp3DiscoveryServiceImplSpec extends AbstractMp3DiscoveryServiceSpec {

    @Override
    def setupMp3DiscoveryService() {
        new LocalMp3DiscoveryServiceImpl(
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
