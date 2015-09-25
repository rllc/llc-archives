package org.llc.archive.service

import org.llc.archive.domain.S3File
import org.llc.archive.filters.FileFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Created by Steven McAdams on 9/24/15.
 */
@Component
class FileFilteringServiceImpl implements FileFilteringService {

    @Autowired
    Collection<FileFilter> filters

    @Override
    List<S3File> filter(List<S3File> s3Files) {
        List<S3File> filteredFiles = []
        s3Files.each { s3File ->
            def passes = true
            filters.each { filter ->
                if (!filter.passes(s3File)) {
                    passes = false
                }
            }
            if (passes) {
                filteredFiles << s3File
            }
        }
        return filteredFiles
    }
}
