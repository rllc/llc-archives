package com.rllc.spreadsheet.service

import com.rllc.spreadsheet.domain.S3File
import com.rllc.spreadsheet.rest.domain.Sermon
import com.rllc.spreadsheet.rest.repository.SermonRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Created by Steven McAdams on 9/24/15.
 */
@Component
class DatabaseCleanupServiceImpl implements DatabaseCleanupService {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseCleanupServiceImpl.class)

    @Autowired
    SermonRepository sermonRepository

    @Override
    void removeDeletedFiles(List<S3File> s3Files, String congregation) {
        List<Sermon> filesToRemove = []
        List<Sermon> sermons = sermonRepository.findByCongregation_Name(congregation)
        sermons.each { sermon ->
            def remove = true
            s3Files.each { s3File ->
                if (sermon.fileUrl.contains(s3File.filename)) {
                    remove = false
                }
            }
            if (remove) {
                filesToRemove << sermon
            }
        }
        logger.info "cleaning up ${filesToRemove.size()} sermons which no longer exist in S3 storage"
        filesToRemove.each { sermon ->
            logger.info("deleting sermon : ${sermon.fileUrl}")
            sermonRepository.delete(sermon.id)
        }
    }
}
