package org.llc.archive.service

import org.llc.archive.domain.RemoteFiles
import org.llc.archive.domain.S3File

/**
 * Created by Steven McAdams on 5/25/15.
 */
interface AmazonService {

    RemoteFiles downloadMetadata(List<String> fileNames, String congregationKey)

    List<S3File> listFiles(String congregationKey)

}