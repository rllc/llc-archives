package com.rllc.spreadsheet.filters

import com.rllc.spreadsheet.domain.S3File
import com.rllc.spreadsheet.rest.repository.SermonRepository
import com.rllc.spreadsheet.rest.repository.SyncExecutionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Created by Steven McAdams on 9/23/15.
 */
@Component
class NewSermonFileFilter implements FileFilter {

    @Autowired
    SyncExecutionRepository syncExecutionRepository

    @Autowired
    SermonRepository sermonRepository

    @Override
    boolean passes(S3File s3File) {
        def pass = false
        Date lastExecution = syncExecutionRepository.findTop1ByOrderByDateDesc()[0]?.date ?: new Date().parse("MM/dd/yyyy", "01/01/2000")
        if (s3File.lastModified.after(lastExecution) || s3File.lastModified.equals(lastExecution)) {
            pass = true
        }
        if (!sermonExists(s3File)) {
            pass = true
        }
        return pass
    }

    boolean sermonExists(S3File s3File) {
        return !sermonRepository.findByFileUrlEndingWith(s3File.filename).isEmpty()
    }

}
