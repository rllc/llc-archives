package com.rllc.spreadsheet.service

import com.rllc.spreadsheet.dao.SermonDAO
import com.rllc.spreadsheet.domain.Column
import com.rllc.spreadsheet.domain.Sermon
import com.rllc.spreadsheet.props.CongregationPropertyLoader
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
    SermonDAO sermonDAO

    @Autowired
    CongregationPropertyLoader congregationPropertyLoader

    @Autowired
    @Resource(name = "localMp3DiscoveryService")
    private Mp3DiscoveryService mp3DiscoveryService;

    @Override
    def updateSpreadsheet() {
        congregationPropertyLoader.congregations.each { congregation ->
            updateSpreadsheet(mp3DiscoveryService.processMp3Files(congregation));
        }
    }

    def updateSpreadsheet(List<Sermon> sermons) {
        logger.info("========= UPDATING SPREADSHEET =========");
        sermons.each { sermon ->
            logger.info "> sermon : ${sermon.filelocation}"
            // if the sermon already exists
            def existingSermon = sermonDAO.get(sermon.filelocation)
            if (existingSermon) {
                Column.values().each { column ->
                    if (existingSermon[column.label].isEmpty() && !sermon[column.label].isEmpty()) {
                        logger.info("> setting {} to {}", column.label, sermon[column.label]);
                        existingSermon[column.label] = sermon[column.label]
                    }
                }
                sermonDAO.update(existingSermon)
            } else {
                sermonDAO.create(sermon)
            }
        }
    }
}
