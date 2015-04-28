package com.rllc.spreadsheet.service

import com.rllc.spreadsheet.dao.SermonDAO
import com.rllc.spreadsheet.domain.Column
import com.rllc.spreadsheet.domain.Sermon
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Created by Steven McAdams on 4/25/15.
 */
@Component
class ArchivedSermonsServiceImpl implements ArchivedSermonsService {

    private static final Logger logger = LoggerFactory.getLogger(ArchivedSermonsServiceImpl.class);

    @Autowired
    SermonDAO sermonDAO

    @Autowired
    private Mp3DiscoveryService mp3DiscoveryService;

    @Override
    def updateSpreadsheet() {
        updateSpreadsheet(mp3DiscoveryService.getMp3s());
    }

    def updateSpreadsheet(List<Sermon> sermons) {
        logger.info("========= UPDATING SPREADSHEET =========");
        sermons.each { sermon ->
            logger.info "> sermon : ${sermon.filelocation}"
            // if the sermon already exists
            def existingSermon = sermonDAO.get(sermon.filelocation)
            if (existingSermon) {
                Column.values().each { column ->
                    if (existingSermon[column.value].isEmpty() && !sermon[column.value].isEmpty()) {
                        logger.info("> setting {} to {}", column.value, sermon[column.value]);
                        existingSermon[column.value] = sermon[column.value]
                    }
                }
                sermonDAO.update(existingSermon)
            } else {
                sermonDAO.create(sermon)
            }
        }
    }
}
