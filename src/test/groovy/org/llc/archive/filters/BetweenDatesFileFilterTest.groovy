package org.llc.archive.filters

import org.llc.archive.domain.S3File
import org.llc.archive.rest.domain.Sermon
import org.llc.archive.rest.repository.SermonRepository
import spock.lang.Specification
import spock.lang.Unroll

class BetweenDatesFileFilterTest extends Specification {

    @Unroll
    def "filter between dates #fromDate-#toDate, #sermons"() {

        def sermonRepository = Mock(SermonRepository)
        sermonRepository.findByFileUrlEndingWith(_ as String) >> sermons

        when: "file is run through filter"
        def betweenDatesFileFilter = new BetweenDatesFileFilter(
                fromDate: fromDate,
                toDate: toDate,
                sermonRepository: sermonRepository
        )

        S3File s3File = new S3File(
                filename: 'blahblahblah',
                lastModified: new Date()
        )

        then:
        betweenDatesFileFilter.passes(s3File) == passes

        where:
        fromDate                                     | toDate                                       | sermons                               || passes
        new Date().parse("MM/dd/yyyy", "01/01/2015") | new Date().parse("MM/dd/yyyy", "02/01/2015") | []                                    || true
        new Date().parse("MM/dd/yyyy", "01/01/2015") | new Date().parse("MM/dd/yyyy", "02/01/2015") | [new Sermon(fileUrl: 'blahblahblah')] || false
        new Date().parse("MM/dd/yyyy", "01/01/2015") | new Date() + 100                             | [new Sermon(fileUrl: 'blahblahblah')] || true

    }


}
