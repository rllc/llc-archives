package org.llc.archive.filters

import org.llc.archive.domain.S3File
import org.llc.archive.rest.domain.Sermon
import org.llc.archive.rest.domain.SyncExecution
import org.llc.archive.rest.repository.SermonRepository
import org.llc.archive.rest.repository.SyncExecutionRepository
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by Steven McAdams on 9/24/15.
 */
class NewSermonFileFilterSpec extends Specification {

    @Unroll
    def "Mp3 file #lastModified"() {

        given: "A last execution time"
        def s3File = new S3File(lastModified: buildDate(lastModified))
        def filter = new NewSermonFileFilter()
        filter.syncExecutionRepository = Mock(SyncExecutionRepository)
        filter.sermonRepository = Mock(SermonRepository)
        filter.syncExecutionRepository.findTop1ByOrderByDateDesc() >> {
            [new SyncExecution(date: new Date().parse('MM/dd/yyyy', '01/01/2015'))]
        }
        filter.sermonRepository.findByFileUrlEndingWith(_) >> { [new Sermon(fileUrl: s3File.filename)] }

        when: "I run the filter on a file"
        def pass = filter.passes(s3File)

        then: "File passes/fails filter"
        pass == passes

        where:
        lastModified || passes
        '01/02/2015' || true
        '01/01/2015' || true
        '01/01/2014' || false
    }

    private Date buildDate(String date) {
        new Date().parse('MM/dd/yyyy', date)
    }

}
