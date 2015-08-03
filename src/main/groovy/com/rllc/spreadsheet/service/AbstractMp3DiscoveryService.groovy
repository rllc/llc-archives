package com.rllc.spreadsheet.service

import com.mpatric.mp3agic.ID3v1
import com.mpatric.mp3agic.Mp3File
import com.rllc.spreadsheet.domain.Mp3SermonFile
import com.rllc.spreadsheet.domain.RemoteFiles
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Created by Steven McAdams on 5/5/15.
 */
@Component
abstract class AbstractMp3DiscoveryService implements Mp3DiscoveryService {

    private static final Logger logger = LoggerFactory.getLogger(AbstractMp3DiscoveryService.class);

    @Autowired
    TextParsingService textParsingService

    abstract RemoteFiles findMp3Files(boolean refreshAll, String congregationKey)

    @Override
    List<Mp3SermonFile> processMp3Files(boolean refreshAll, String congregationKey) {
        def sermonList = []
        RemoteFiles mp3Results = findMp3Files(refreshAll, congregationKey)
        if (mp3Results) {
            def root = mp3Results.root
            List mp3Files = mp3Results.files
            mp3Files.each { File mp3FileHandle ->
                logger.debug "> ${mp3FileHandle.name}"
                try {
                    def mp3file = new Mp3File(mp3FileHandle.absolutePath);
                    def id3v1Tag = mp3file.hasId3v1Tag() ? mp3file.id3v1Tag : mp3file.id3v2Tag
                    sermonList << extractId3v1TagData(root, mp3FileHandle, id3v1Tag)
                } catch (all) {
                    logger.warn "> ${mp3FileHandle.name} : ${all.message}"
                    sermonList << new Mp3SermonFile(
                            date: textParsingService.parseDateFromFilename(mp3FileHandle.name),
                            minister: textParsingService.parseMinisterFromFilename(mp3FileHandle.name),
                            file: textParsingService.parseFilename(root, mp3FileHandle.absolutePath)
                    )
                }
            }
        }
        sermonList
    }

    @Override
    Mp3SermonFile extractId3v1TagData(String basePath, File mp3FileHandle, ID3v1 id3v1Tag) {
        new Mp3SermonFile(
                file: textParsingService.parseFilename(basePath, mp3FileHandle.absolutePath),
                date: textParsingService.parseDate(id3v1Tag.title, mp3FileHandle.name),
                bibletext: textParsingService.parseBibleText(id3v1Tag.album),
                minister: textParsingService.parseMinister(id3v1Tag.artist),
                comments: textParsingService.parseNotes(id3v1Tag.comment)

        )
    }
}
