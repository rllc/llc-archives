package org.llc.archive.service

import org.llc.archive.domain.Mp3SermonFile
import org.llc.archive.props.CongregationPropertyLoader
import org.llc.archive.rest.domain.Sermon
import org.llc.archive.rest.domain.SyncExecution
import org.llc.archive.rest.repository.CongregationRepository
import org.llc.archive.rest.repository.SermonRepository
import org.llc.archive.rest.repository.SyncExecutionRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class ArchivedSermonsService {

    private static final Logger logger = LoggerFactory.getLogger(ArchivedSermonsService.class);
    public static final String AMAZON_S3_URL = "https://s3.amazonaws.com"

    @Autowired
    SermonRepository sermonRepository

    @Autowired
    CongregationRepository congregationRepository

    @Autowired
    SyncExecutionRepository syncExecutionRepository

    @Autowired
    CongregationPropertyLoader congregationPropertyLoader

    @Autowired
    private RemoteMp3DiscoveryService mp3DiscoveryService;

    def updateDatastore(Date fromDate, Date toDate, List<String> congregations) {
        logger.info("> refreshing LLC sermon database");
        def start = System.currentTimeMillis()

        congregationPropertyLoader.credentials.findAll { it.key in congregations }.each { name, creds ->
            logger.info("========= ${name} =========");
            writeToDatabase(name, mp3DiscoveryService.processMp3Files(fromDate, toDate, name));
            logger.info("... done!");
        }

        def now = System.currentTimeMillis()
        def duration = now - start
        // delete old sync execution records from the database
        syncExecutionRepository.deleteAll()
        syncExecutionRepository.save(
                new SyncExecution(
                        username: SecurityContextHolder.context.authentication.principal.username,
                        date: new Date(),
                        executionTimeMs: duration
                )
        )
    }

    def writeToDatabase(String name, List<Mp3SermonFile> sermons) {
        def bucket = congregationPropertyLoader.credentials[name].amazonCredentials.bucket
        sermons.each { sermon ->
            logger.debug "> sermon : ${sermon.file}"

            Sermon sermonToUpdate = getExistingSermonOrCreateNew(sermon)
            sermonToUpdate.bibleText = sermon.bibletext
            sermonToUpdate.comments = sermon.comments
            sermonToUpdate.congregation = congregationRepository.findByName(name)[0]
            sermonToUpdate.date = sermon.date ? new Date().parse("MM/dd/yyyy", sermon.date) : null
            sermonToUpdate.fileUrl = AMAZON_S3_URL + "/${bucket}/${sermon.file}"
            sermonToUpdate.minister = sermon.minister
            sermonRepository.save(sermonToUpdate)

        }
    }

    private Sermon getExistingSermonOrCreateNew(Mp3SermonFile sermon) {
        def existingSermons = sermonRepository.findByFileUrlEndingWith(sermon.file)
        if (existingSermons.size() > 0) {
            return existingSermons[0]
        } else {
            return new Sermon()
        }
    }
}
