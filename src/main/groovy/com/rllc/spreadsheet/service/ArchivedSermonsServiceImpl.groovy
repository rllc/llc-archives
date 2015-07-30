package com.rllc.spreadsheet.service

import com.rllc.spreadsheet.domain.Mp3SermonFile
import com.rllc.spreadsheet.props.CongregationPropertyLoader
import com.rllc.spreadsheet.rest.domain.Sermon
import com.rllc.spreadsheet.rest.repository.CongregationRepository
import com.rllc.spreadsheet.rest.repository.MinisterRepository
import com.rllc.spreadsheet.rest.repository.SermonRepository
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
    SermonRepository sermonRepository

    @Autowired
    CongregationRepository congregationRepository

    @Autowired
    MinisterRepository ministerRepository

    @Autowired
    CongregationPropertyLoader congregationPropertyLoader

    @Autowired
    private Mp3DiscoveryService mp3DiscoveryService;

    @Override
    def updateDatastore() {
        logger.info("> refreshing LLC sermon database");
        congregationPropertyLoader.credentials.each { name, creds ->
            logger.info("========= ${name} =========");
            updateDatastore(name, mp3DiscoveryService.processMp3Files(name));
        }
    }

    def updateDatastore(String name, List<Mp3SermonFile> sermons) {
        def bucket = congregationPropertyLoader.credentials[name].amazonCredentials.bucket
        sermons.each { sermon ->
            logger.info "> sermon : ${sermon.file}"

            def minister = null
            if (sermon.minister) {
                logger.info "> minister : ${sermon.minister}"
                def tokens = sermon.minister.split()
                logger.info "> tokens : $tokens, tokens.size : ${tokens.size()}"
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
                existingSermon.date = sermon.date
                existingSermon.fileUrl = "https://s3-us-west-2.amazonaws.com/${bucket}/${sermon.file}"
                existingSermon.minister = minister ? minister.naturalName : ''
                sermonRepository.save(existingSermon)
            } else {

                Sermon newSermon = new Sermon(
                        minister: minister ? minister.naturalName : '',
                        bibleText: sermon.bibletext,
                        comments: sermon.comments,
                        date: sermon.date ? new Date().parse("MM/dd/yyyy", sermon.date) : null,
                        congregation: congregationRepository.findByName(name)[0],
                        fileUrl: "https://s3-us-west-2.amazonaws.com/${bucket}/${sermon.file}"
                )
                sermonCrudRepository.save(newSermon)
            }


        }
    }
}
