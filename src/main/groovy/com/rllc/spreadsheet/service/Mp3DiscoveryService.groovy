package com.rllc.spreadsheet.service

import com.mpatric.mp3agic.ID3v1
import com.rllc.spreadsheet.domain.Sermon

/**
 * Created by Steven McAdams on 4/25/15.
 */
interface Mp3DiscoveryService {
    List<Sermon> getMp3s();

    Sermon extractId3v1TagData(File mp3FileHandle, ID3v1 id3v1Tag)
}
