package org.llc.archive.service

import org.llc.archive.domain.Mp3SermonFile

/**
 * Created by Steven McAdams on 4/25/15.
 */
interface Mp3DiscoveryService {

    List<Mp3SermonFile> processMp3Files(Date fromDate, Date toDate, String congregationKey)

}
