package org.llc.archive.filters

import org.llc.archive.domain.S3File
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by Steven McAdams on 9/24/15.
 */
class Mp3FileFilterSpec extends Specification {

    @Unroll
    def "Mp3 file #filename passes ? #passes"() {

        given: "An instance of Mp3 File Filter"
        def file = new S3File(
                filename: filename
        )
        def filter = new Mp3FileFilter()

        when: "I run the filter on a file"
        def pass = filter.passes(file)

        then: "File passes/fails filter"
        pass == passes

        where:
        filename || passes
        'a.mp3'  || true
        'a.Mp3'  || true
        'a.MP3'  || true
        'a.mP3'  || true
        'a.ra'   || false
        'a.aac'  || false
        'a.mp4'  || false
    }
}
