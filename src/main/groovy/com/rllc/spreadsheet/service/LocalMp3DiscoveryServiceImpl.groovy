package com.rllc.spreadsheet.service

import com.rllc.spreadsheet.domain.Congregation
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * Created by Steven McAdams on 4/25/15.
 */
@Component(value="localMp3DiscoveryService")
class LocalMp3DiscoveryServiceImpl extends AbstractMp3DiscoveryService {

    private static final Logger logger = LoggerFactory.getLogger(LocalMp3DiscoveryServiceImpl.class);

    List<File> findMp3Files(Congregation congregation) {
        def mp3Files = []
        new File(congregation.mp3Directory).eachDirRecurse() { dir ->
            dir.eachFileMatch(~/.*.mp3/) { file ->
                mp3Files << file
            }
        }
        mp3Files
    }

}
