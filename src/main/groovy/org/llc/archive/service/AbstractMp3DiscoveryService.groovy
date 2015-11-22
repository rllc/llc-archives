package org.llc.archive.service

import org.jaudiotagger.audio.AudioFile
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import org.jaudiotagger.tag.Tag
import org.llc.archive.domain.Mp3SermonFile
import org.llc.archive.domain.RemoteFiles
import org.llc.archive.domain.S3File
import org.llc.archive.filters.Mp3FileFilter
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

    @Autowired
    FileFilteringService fileFilteringService

    @Autowired
    DatabaseCleanupService databaseCleanupService

    @Autowired
    Mp3FileFilter mp3FileFilter

    abstract List<S3File> findMp3Files(String congregationKey)

    abstract RemoteFiles downloadMetadata(List<S3File> s3Files, String congregationKey)

    @Override
    List<Mp3SermonFile> processMp3Files(boolean refreshAll, String congregationKey) {
        def sermonList = []
        List<S3File> allFiles = findMp3Files(congregationKey)
        List<S3File> filteredFiles
        if (refreshAll) {
            filteredFiles = allFiles.findAll { mp3FileFilter.passes(it) }
        } else {
            filteredFiles = fileFilteringService.filter(allFiles)
        }

        databaseCleanupService.removeDeletedFiles(allFiles, congregationKey)
        RemoteFiles mp3Results = downloadMetadata(filteredFiles, congregationKey)
        if (mp3Results) {
            def root = mp3Results.root
            List mp3Files = mp3Results.files
            mp3Files.each { File mp3FileHandle ->
                logger.debug "> ${mp3FileHandle.name}"
                try {
                    AudioFile mp3File = AudioFileIO.read(mp3FileHandle)
                    sermonList << extractId3v1TagData(root, mp3FileHandle, mp3File.tag)
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

    Mp3SermonFile extractId3v1TagData(String basePath, File mp3FileHandle, Tag tag) {
        new Mp3SermonFile(
                file: textParsingService.parseFilename(basePath, mp3FileHandle.absolutePath),
                date: textParsingService.parseDate(extractTag(tag, [FieldKey.TITLE]), mp3FileHandle.name),
                bibletext: textParsingService.parseBibleText(extractTag(tag, [FieldKey.ALBUM])),
                minister: textParsingService.parseMinister(extractTag(tag, [FieldKey.ARTIST, FieldKey.ALBUM_ARTIST])),
                comments: textParsingService.parseNotes(extractTag(tag, [FieldKey.COMMENT]))

        )
    }

    private extractTag(Tag tag, List<FieldKey> keys) {
        def value = ''
        try {
            keys.find { key ->
                value = tag.getFirst(key)
                return !value.isEmpty()
            }
        }
        catch (all) {
            logger.warn "> no ${keys.collect { it.name() }.join(', ')} found in mp3 tag"
        }

        return value
    }
}
