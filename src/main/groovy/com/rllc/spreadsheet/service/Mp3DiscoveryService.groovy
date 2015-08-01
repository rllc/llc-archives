package com.rllc.spreadsheet.service

import com.mpatric.mp3agic.ID3v1
import com.rllc.spreadsheet.domain.Mp3SermonFile
import com.rllc.spreadsheet.domain.RemoteFiles

/**
 * Created by Steven McAdams on 4/25/15.
 */
interface Mp3DiscoveryService {

    RemoteFiles findMp3Files(boolean refreshAll, String congregationKey)

    List<Mp3SermonFile> processMp3Files(boolean refreshAll, String congregationKey)

    Mp3SermonFile extractId3v1TagData(String basePath, File mp3FileHandle, ID3v1 id3v1Tag)
}
