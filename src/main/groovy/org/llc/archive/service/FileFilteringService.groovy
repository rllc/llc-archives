package org.llc.archive.service

import org.llc.archive.domain.S3File

/**
 * Created by Steven McAdams on 9/24/15.
 */
interface FileFilteringService {

    List<S3File> filter(List<S3File> s3Files)

}