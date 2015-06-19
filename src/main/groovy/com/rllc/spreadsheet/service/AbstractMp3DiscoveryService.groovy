package com.rllc.spreadsheet.service

import com.mpatric.mp3agic.ID3v1
import com.mpatric.mp3agic.InvalidDataException
import com.mpatric.mp3agic.Mp3File
import com.mpatric.mp3agic.UnsupportedTagException
import com.rllc.spreadsheet.rest.domain.Congregation
import com.rllc.spreadsheet.rest.domain.Sermon
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

    abstract List<File> findMp3Files(Congregation congregation)

    @Override
    List<Sermon> processMp3Files(Congregation congregation) {
        def sermonList = []
        List mp3Files = findMp3Files(congregation)
        mp3Files.each { mp3FileHandle ->
            logger.debug "> ${mp3FileHandle.name}"
            try {
                def mp3file = new Mp3File(mp3FileHandle.absolutePath);
                def id3v1Tag = mp3file.hasId3v1Tag() ? mp3file.id3v1Tag : mp3file.id3v2Tag
                sermonList << extractId3v1TagData(congregation.mp3Directory, mp3FileHandle, id3v1Tag)
            } catch (IOException e) {
                e.printStackTrace();
            } catch (UnsupportedTagException e) {
                e.printStackTrace();
            } catch (InvalidDataException e) {
                e.printStackTrace();
            }
        }
        sermonList
    }

    @Override
    Sermon extractId3v1TagData(String mp3Directory, File mp3FileHandle, ID3v1 id3v1Tag) {
        new Sermon(
                filelocation: textParsingService.parseFilename(mp3Directory, mp3FileHandle.absolutePath),
                date: textParsingService.parseDate(id3v1Tag.title, mp3FileHandle.name),
                time: textParsingService.parseTime(id3v1Tag.title),
                bibletext: textParsingService.parseBibleText(id3v1Tag.album),
                minister: textParsingService.parseMinister(id3v1Tag.artist),
                notes: textParsingService.parseNotes(id3v1Tag.comment)
        )
    }
}
