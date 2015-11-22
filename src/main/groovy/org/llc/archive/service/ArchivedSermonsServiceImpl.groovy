package org.llc.archive.service

import org.llc.archive.domain.Mp3SermonFile
import org.llc.archive.props.CongregationPropertyLoader
import org.llc.archive.rest.domain.Sermon
import org.llc.archive.rest.domain.SyncExecution
import org.llc.archive.rest.repository.CongregationRepository
import org.llc.archive.rest.repository.MinisterRepository
import org.llc.archive.rest.repository.SermonRepository
import org.llc.archive.rest.repository.SyncExecutionRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

/**
 * Created by Steven McAdams on 4/25/15.
 */
@Component
class ArchivedSermonsServiceImpl implements ArchivedSermonsService {

    private static final Logger logger = LoggerFactory.getLogger(ArchivedSermonsServiceImpl.class);

    @Autowired
    SermonRepository sermonRepository

    @Autowired
    CongregationRepository congregationRepository

    @Autowired
    MinisterRepository ministerRepository

    @Autowired
    SyncExecutionRepository syncExecutionRepository

    @Autowired
    CongregationPropertyLoader congregationPropertyLoader

    @Autowired
    private Mp3DiscoveryService mp3DiscoveryService;

    @Override
    def updateDatastore(boolean refreshAll) {
        logger.info("> refreshing LLC sermon database");
        def start = System.currentTimeMillis()
        congregationPropertyLoader.credentials.each { name, creds ->
            logger.info("========= ${name} =========");
            updateDatastore(name, mp3DiscoveryService.processMp3Files(refreshAll, name));
        }
        def now = System.currentTimeMillis()
        def duration = now - start
        syncExecutionRepository.save(
                new SyncExecution(
                        username: SecurityContextHolder.context.authentication.principal.username,
                        date: new Date(),
                        executionTimeMs: duration
                )
        )
    }

    def updateDatastore(String name, List<Mp3SermonFile> sermons) {
        def bucket = congregationPropertyLoader.credentials[name].amazonCredentials.bucket
        sermons.each { sermon ->
            logger.debug "> sermon : ${sermon.file}"

            def minister = null
            if (sermon.minister) {
                logger.debug "> minister : ${sermon.minister}"
                def tokens = sermon.minister.split()
                if (tokens.size() == 2) {
                    def firstName = tokens[0]
                    def lastName = tokens[1]
                    minister = ministerRepository.findByFirstNameAndLastNameLike(firstName, lastName)[0]
                }
            }
            def existingSermons = sermonRepository.findByFileUrlEndingWith(sermon.file)
            if (existingSermons.size() > 0) {
                def existingSermon = existingSermons[0]
                existingSermon.bibleText = sermon.bibletext
                existingSermon.comments = sermon.comments
                existingSermon.congregation = congregationRepository.findByName(name)[0]
                existingSermon.date = sermon.date ? new Date().parse("MM/dd/yyyy", sermon.date) : null
                existingSermon.fileUrl = "https://s3.amazonaws.com/${bucket}/${sermon.file}"
                existingSermon.minister = minister ? minister.naturalName : ''
                sermonRepository.save(existingSermon)
            } else {

                Sermon newSermon = new Sermon(
                        minister: minister ? minister.naturalName : '',
                        bibleText: sermon.bibletext,
                        comments: sermon.comments,
                        date: sermon.date ? new Date().parse("MM/dd/yyyy", sermon.date) : null,
                        congregation: congregationRepository.findByName(name)[0],
                        fileUrl: "https://s3.amazonaws.com/${bucket}/${sermon.file}"
                )
                sermonRepository.save(newSermon)
            }


        }
    }
}
