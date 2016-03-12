package org.llc.archive.filters

import org.llc.archive.domain.S3File

/**
 * Created by Steven McAdams on 9/24/15.
 */
class BetweenDatesFileFilter implements FileFilter {

    Date fromDate

    Date toDate

    @Override
    boolean passes(S3File s3File) {
        return s3File.lastModified.after(fromDate) && s3File.lastModified.before(toDate)
    }
}
