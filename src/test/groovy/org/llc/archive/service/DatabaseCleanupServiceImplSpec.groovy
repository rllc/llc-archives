package org.llc.archive.service

import org.llc.archive.domain.S3File
import org.llc.archive.rest.domain.Congregation
import org.llc.archive.rest.domain.Sermon
import org.llc.archive.rest.repository.CongregationRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by Steven McAdams on 9/24/15.
 */
class DatabaseCleanupServiceImplSpec extends Specification {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseCleanupServiceImplSpec.class)

    @Shared
    CongregationRepository congregationRepository

    @Shared
    DatabaseCleanupService databaseCleanupService

    @Shared
    int numberOfSermonsSaved

    def setupSpec() {
        congregationRepository = Mock(CongregationRepository)
        congregationRepository./findBy.*/(_) >> {
            [
                    new Congregation(
                            id: 1,
                            sermons: [
                                    new Sermon(congregation: [id: 1], id: 1, fileUrl: "https://s3-us-west-2.amazonaws.com/rllcarchives/2015/20150101_RNikula.mp3"),
                                    new Sermon(congregation: [id: 1], id: 2, fileUrl: "https://s3-us-west-2.amazonaws.com/rllcarchives/2015/20150104_MLee.mp3"),
                                    new Sermon(congregation: [id: 1], id: 3, fileUrl: "https://s3-us-west-2.amazonaws.com/rllcarchives/2015/20150115_RWaaraniemi.mp3"),
                                    new Sermon(congregation: [id: 1], id: 4, fileUrl: "https://s3-us-west-2.amazonaws.com/rllcarchives/2015/20150118_JHaapsaari.mp3")
                            ]
                    )
            ]
        }

        congregationRepository.save(_ as Congregation) >> { c ->
            numberOfSermonsSaved = c[0].sermons.size()
        }

        databaseCleanupService = new DatabaseCleanupService(
                congregationRepository: congregationRepository
        )
    }

    def setup() {
        numberOfSermonsSaved = 0
    }

    @Unroll
    def "cleanup - number of files remaining: #numberOfFilesRemaining"() {

        given: "A complete list of files that exist on amazon S3 buckets"
        def s3Files = remoteFiles.collect { new S3File(filename: it) }

        when: "database cleanup service is called"
        databaseCleanupService.removeDeletedFiles(s3Files, 'rllc')

        then: "any files which do not exist on amazon S3 are purged from the database"
        numberOfFilesRemaining == numberOfSermonsSaved

        where:
        remoteFiles                                                                                          | numberOfFilesRemaining
        ['20150101_RNikula.mp3']                                                                             | 1
        ['20150101_RNikula.mp3', '20150104_MLee.mp3']                                                        | 2
        ['20150101_RNikula.mp3', '20150104_MLee.mp3', '20150115_RWaaraniemi.mp3']                            | 3
        ['20150101_RNikula.mp3', '20150104_MLee.mp3', '20150115_RWaaraniemi.mp3', '20150118_JHaapsaari.mp3'] | 4


    }
}
