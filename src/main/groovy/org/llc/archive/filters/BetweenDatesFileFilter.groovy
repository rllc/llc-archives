package org.llc.archive.filters

import org.llc.archive.domain.S3File
import org.llc.archive.rest.repository.SermonRepository

/**
 * Created by Steven McAdams on 9/24/15.
 */
class BetweenDatesFileFilter implements FileFilter {

    Date fromDate
    Date toDate
    SermonRepository sermonRepository

    @Override
    boolean passes(S3File s3File) {
        return !sermonRepository.findByFileUrlEndingWith(s3File.filename) ||
                (s3File.lastModified.after(fromDate) && s3File.lastModified.before(toDate))
    }
}
