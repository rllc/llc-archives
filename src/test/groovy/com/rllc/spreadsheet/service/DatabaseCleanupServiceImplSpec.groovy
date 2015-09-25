package com.rllc.spreadsheet.service

import com.rllc.spreadsheet.domain.S3File
import com.rllc.spreadsheet.rest.domain.Sermon
import com.rllc.spreadsheet.rest.repository.SermonRepository
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by Steven McAdams on 9/24/15.
 */
class DatabaseCleanupServiceImplSpec extends Specification {

    @Shared
    SermonRepository sermonRepository

    @Shared
    DatabaseCleanupService databaseCleanupService

    @Shared
    int numberOfDeleteCalls

    def setupSpec() {
        sermonRepository = Mock(SermonRepository)
        sermonRepository.findByCongregation_Name(_) >> {
            [
                    new Sermon(id: 1, fileUrl: "https://s3-us-west-2.amazonaws.com/rllcarchives/2015/20150101_RNikula.mp3"),
                    new Sermon(id: 2, fileUrl: "https://s3-us-west-2.amazonaws.com/rllcarchives/2015/20150104_MLee.mp3"),
                    new Sermon(id: 3, fileUrl: "https://s3-us-west-2.amazonaws.com/rllcarchives/2015/20150115_RWaaraniemi.mp3"),
                    new Sermon(id: 4, fileUrl: "https://s3-us-west-2.amazonaws.com/rllcarchives/2015/20150118_JHaapsaari.mp3")
            ]
        }

        sermonRepository.delete(_) >> {
            numberOfDeleteCalls++
        }

        databaseCleanupService = new DatabaseCleanupServiceImpl(
                sermonRepository: sermonRepository
        )
    }

    def setup() {
        numberOfDeleteCalls = 0
    }

    @Unroll
    def "clean up #numberOfFilesDeleted files"() {

        given: "A complete list of files that exist on amazon S3 buckets"
        def s3Files = remoteFiles.collect { new S3File(filename: it) }

        when: "database cleanup service is called"
        databaseCleanupService.removeDeletedFiles(s3Files, 'rllc')

        then: "any files which do not exist on amazon S3 are purged from the database"
        numberOfFilesDeleted == numberOfDeleteCalls

        where:
        remoteFiles                                                                                          | numberOfFilesDeleted
        ['20150101_RNikula.mp3']                                                                             | 3
        ['20150101_RNikula.mp3', '20150104_MLee.mp3']                                                        | 2
        ['20150101_RNikula.mp3', '20150104_MLee.mp3', '20150115_RWaaraniemi.mp3']                            | 1
        ['20150101_RNikula.mp3', '20150104_MLee.mp3', '20150115_RWaaraniemi.mp3', '20150118_JHaapsaari.mp3'] | 0


    }
}
