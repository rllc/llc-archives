package com.rllc.spreadsheet.service

import com.rllc.spreadsheet.domain.Mp3SermonFile

/**
 * Created by Steven McAdams on 4/25/15.
 */
interface Mp3DiscoveryService {

    List<Mp3SermonFile> processMp3Files(boolean refreshAll, String congregationKey)

}
