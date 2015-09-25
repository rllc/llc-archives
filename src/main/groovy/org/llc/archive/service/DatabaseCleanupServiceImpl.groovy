package org.llc.archive.service

import org.llc.archive.domain.S3File
import org.llc.archive.rest.domain.Congregation
import org.llc.archive.rest.domain.Sermon
import org.llc.archive.rest.repository.CongregationRepository
import org.llc.archive.rest.repository.SermonRepository
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

    @Autowired
    CongregationRepository congregationRepository

    @Override
    void removeDeletedFiles(List<S3File> s3Files, String congregationName) {
        List<Sermon> filesToRemove = []
        List<Sermon> sermons = congregationRepository.findByName(congregationName)[0].sermons
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
        Congregation congregation = congregationRepository.findByName(congregationName)[0]
        filesToRemove.each { sermon ->
            logger.info("deleting sermon : ${sermon.fileUrl}")
            congregation.sermons.removeAll { it.id == sermon.id }
        }
        congregationRepository.save(congregation)
    }
}
