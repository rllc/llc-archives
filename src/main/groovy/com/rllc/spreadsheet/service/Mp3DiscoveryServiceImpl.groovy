package com.rllc.spreadsheet.service

import com.mpatric.mp3agic.ID3v1
import com.mpatric.mp3agic.InvalidDataException
import com.mpatric.mp3agic.Mp3File
import com.mpatric.mp3agic.UnsupportedTagException
import com.rllc.spreadsheet.domain.Sermon
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value

import javax.annotation.PostConstruct

/**
 * Created by Steven McAdams on 4/25/15.
 */
class Mp3DiscoveryServiceImpl implements Mp3DiscoveryService {

    private static final Logger logger = LoggerFactory.getLogger(Mp3DiscoveryServiceImpl.class);

    @Autowired
    TextParsingService textParsingService

    @Value("\${mp3.directory}")
    String mp3Directory;

    @PostConstruct
    public void init() {
        logger.info("scanning mp3 directory [{}]", mp3Directory);
    }

    @Override
    List<Sermon> getMp3s() {
        List mp3Files = findMp3Files()
        processMp3Files(mp3Files)
    }

    @Override
    List<File> findMp3Files() {
        def mp3Files = []
        new File(mp3Directory).eachDirRecurse() { dir ->
            dir.eachFileMatch(~/.*.mp3/) { file ->
                mp3Files << file
            }
        }
        mp3Files
    }

    @Override
    List<Sermon> processMp3Files(List mp3Files) {
        def sermonList = []
        mp3Files.each { mp3FileHandle ->
            logger.debug "> ${mp3FileHandle.name}"
            try {
                def mp3file = new Mp3File(mp3FileHandle.absolutePath);
                def id3v1Tag = mp3file.hasId3v1Tag() ? mp3file.id3v1Tag : mp3file.id3v2Tag
                sermonList << extractId3v1TagData(mp3FileHandle, id3v1Tag)
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
    Sermon extractId3v1TagData(File mp3FileHandle, ID3v1 id3v1Tag) {
        new Sermon(
                filelocation: textParsingService.parseFilename(mp3FileHandle.absolutePath),
                date: textParsingService.parseDate(id3v1Tag.title, mp3FileHandle.name),
                time: textParsingService.parseTime(id3v1Tag.title),
                bibletext: textParsingService.parseBibleText(id3v1Tag.album),
                minister: textParsingService.parseMinister(id3v1Tag.artist)
        )
    }
}
