package org.llc.archive.service

import org.llc.archive.domain.RemoteFile
import org.llc.archive.domain.S3File

/**
 * Created by Steven McAdams on 5/25/15.
 */
interface AmazonService {

    RemoteFile downloadMetadata(String fileName, String congregationKey)

    List<S3File> listFiles(String congregationKey)

}