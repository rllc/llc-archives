package org.llc.archive.filters

import org.llc.archive.domain.S3File

/**
 * Created by Steven McAdams on 9/23/15.
 */
interface FileFilter {
    boolean passes(S3File s3File)
}
