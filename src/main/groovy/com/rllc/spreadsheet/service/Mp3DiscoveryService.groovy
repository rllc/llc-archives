package com.rllc.spreadsheet.service

import com.mpatric.mp3agic.ID3v1
import com.rllc.spreadsheet.rest.domain.Congregation
import com.rllc.spreadsheet.rest.domain.Sermon

/**
 * Created by Steven McAdams on 4/25/15.
 */
interface Mp3DiscoveryService {

    List<File> findMp3Files(Congregation congregation)

    List<Sermon> processMp3Files(Congregation congregation)

    Sermon extractId3v1TagData(String mp3Directory, File mp3FileHandle, ID3v1 id3v1Tag)
}
