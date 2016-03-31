package org.llc.archive.service

import org.jaudiotagger.audio.AudioFile
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import org.jaudiotagger.tag.Tag
import org.llc.archive.domain.Mp3SermonFile
import org.llc.archive.domain.RemoteFile
import org.llc.archive.domain.S3File
import org.llc.archive.filters.BetweenDatesFileFilter
import org.llc.archive.filters.FileFilter
import org.llc.archive.filters.Mp3FileFilter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
abstract class AbstractMp3DiscoveryService {

    private static final Logger logger = LoggerFactory.getLogger(AbstractMp3DiscoveryService.class);

    @Autowired
    TextParsingService textParsingService

    @Autowired
    FileFilteringService fileFilteringService

    @Autowired
    DatabaseCleanupService databaseCleanupService

    abstract List<S3File> findMp3Files(String congregationKey)

    abstract RemoteFile downloadMetadata(S3File s3File, String congregationKey)

    List<Mp3SermonFile> processMp3Files(Date fromDate, Date toDate, String congregationKey) {
        List<S3File> allFiles = findMp3Files(congregationKey)
        databaseCleanupService.removeDeletedFiles(allFiles, congregationKey)

        List<FileFilter> fileFilters = [
                new Mp3FileFilter(),
                new BetweenDatesFileFilter(
                        fromDate: fromDate,
                        toDate: toDate
                )
        ]


        List<Mp3SermonFile> mp3SermonFileList = []

        def filteredFiles = fileFilteringService.filter(allFiles, fileFilters)
        logger.info "filteredFiles size : ${filteredFiles.size()}"
        filteredFiles.each { S3File file ->
            RemoteFile remoteFile = downloadMetadata(file, congregationKey)
            mp3SermonFileList << extractMp3Data(remoteFile)
        }

        return mp3SermonFileList
    }

    Mp3SermonFile extractMp3Data(RemoteFile remoteFile) {
        Mp3SermonFile mp3SermonFile
        if (remoteFile && remoteFile.file && remoteFile.root) {
            logger.debug "> ${remoteFile.file.name}"
            try {
                AudioFile mp3File = AudioFileIO.read(remoteFile.file)
                mp3SermonFile = extractId3v1TagData(remoteFile.root, remoteFile.file, mp3File.tag)
            } catch (all) {
                logger.warn "> ${remoteFile.file.name} : ${all.message}"
                mp3SermonFile = new Mp3SermonFile(
                        date: textParsingService.parseDateFromFilename(remoteFile.file.name),
                        minister: textParsingService.parseMinisterFromFilename(remoteFile.file.name),
                        file: textParsingService.parseFilename(remoteFile.root, remoteFile.file.absolutePath)
                )
            }
        }

        return mp3SermonFile
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

    private static extractTag(Tag tag, List<FieldKey> keys) {
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
