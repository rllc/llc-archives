package org.llc.archive.service

import org.llc.archive.domain.S3File
import org.llc.archive.filters.FileFilter

/**
 * Created by Steven McAdams on 9/24/15.
 */
interface FileFilteringService {

    List<S3File> filter(List<S3File> s3Files, Collection<FileFilter> filters)

}