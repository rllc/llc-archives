package com.rllc.spreadsheet.service

import com.rllc.spreadsheet.props.CongregationPropertyLoader
import com.rllc.spreadsheet.rest.domain.Sermon
import com.rllc.spreadsheet.rest.repository.SermonCrudRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.annotation.Resource

/**
 * Created by Steven McAdams on 4/25/15.
 */
@Component
class ArchivedSermonsServiceImpl implements ArchivedSermonsService {

    private static final Logger logger = LoggerFactory.getLogger(ArchivedSermonsServiceImpl.class);

    @Autowired
    SermonCrudRepository sermonCrudRepository

    @Autowired
    CongregationPropertyLoader congregationPropertyLoader

    @Autowired
    @Resource(name = "localMp3DiscoveryService")
    private Mp3DiscoveryService mp3DiscoveryService;

    @Override
    def updateDatastore() {
        logger.info("> refreshing LLC sermon database");
        congregationPropertyLoader.congregations.each { congregation ->
            logger.info("========= ${congregation.longName} =========");
            updateDatastore(mp3DiscoveryService.processMp3Files(congregation));
        }
    }

    def updateDatastore(List<Sermon> sermons) {
        sermons.each { sermon ->
            logger.info "> sermon : ${sermon.fileUrl}"
            sermonCrudRepository.save(sermons)
        }
    }
}
