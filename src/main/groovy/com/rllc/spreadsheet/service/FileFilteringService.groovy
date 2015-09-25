package com.rllc.spreadsheet.service

import com.rllc.spreadsheet.domain.S3File

/**
 * Created by Steven McAdams on 9/24/15.
 */
interface FileFilteringService {

    List<S3File> filter(List<S3File> s3Files)

}