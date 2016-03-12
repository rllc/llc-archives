package org.llc.archive.filters

import org.llc.archive.domain.S3File

/**
 * Created by Steven McAdams on 9/24/15.
 */
class Mp3FileFilter implements FileFilter {
    @Override
    boolean passes(S3File s3File) {
        return s3File.filename.toLowerCase().endsWith('.mp3')
    }
}
