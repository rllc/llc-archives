package com.rllc.spreadsheet.filters

import com.rllc.spreadsheet.domain.S3File
import org.springframework.stereotype.Component

/**
 * Created by Steven McAdams on 9/24/15.
 */
@Component
class Mp3FileFilter implements FileFilter {
    @Override
    boolean passes(S3File s3File) {
        return s3File.filename.toLowerCase().endsWith('.mp3')
    }
}
