package com.rllc.spreadsheet.filters

import com.rllc.spreadsheet.domain.S3File

/**
 * Created by Steven McAdams on 9/23/15.
 */
interface FileFilter {
    boolean passes(S3File s3File)
}
