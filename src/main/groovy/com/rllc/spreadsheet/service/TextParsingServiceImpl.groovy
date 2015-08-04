package com.rllc.spreadsheet.service

import com.rllc.spreadsheet.rest.domain.Minister
import com.rllc.spreadsheet.rest.repository.MinisterRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import java.text.ParseException
import java.text.SimpleDateFormat

/**
 * Created by Steven McAdams on 4/25/15.
 */
@Component
class TextParsingServiceImpl implements TextParsingService {

    private static final Logger logger = LoggerFactory.getLogger(TextParsingServiceImpl.class)

    @Autowired
    MinisterRepository ministerRepository

    @Override
    String parseFilename(String basePath, String absoluteFilePath) {
        def filename = absoluteFilePath
                .replace("\\", "/")
                .replace(basePath, "")
        if (filename.startsWith("/")) {
            filename = filename.substring(1)
        }
        return filename
    }

    @Override
    String parseMinister(String artist) {
        List<String> ministers = ministerRepository.findAll().collect { "${it.firstName} ${it.lastName}" }
        String minister = artist.toLowerCase().split().collect { it.capitalize() }.join(' ')
        List<String> similarMinisters = CosineSimilarityService.mostSimilar(minister, ministers, 0.4)
        if (similarMinisters.size() > 0) {
            minister = similarMinisters.get(0);
        }

        if (artist != minister) {
            logger.info "{} -> {}", artist, minister
        }
        return minister
    }

    @Override
    String parseMinisterFromFilename(String filename) {

        def tokens = filename.split("_")
        if (tokens.size() == 2) {
            def name = tokens[1].substring(0, tokens[1].length() - 4)

            def firstName = name[0]
            def lastName = name.substring(1, name.length())
            def naturalName = "$firstName $lastName"

            List<Minister> ministers = ministerRepository.findByLastName(lastName)
            if (ministers.size() == 1) {
                return ministers[0].naturalName
            } else {
                return parseMinister(naturalName)
            }
        }

    }

    @Override
    String parseBibleText(String album) {
        return album ?: ""
    }

    @Override
    String parseTime(String title) {
        String time = ""
        title = title.replaceAll("am", "").replaceAll("AM", "")
        String[] tokens = title.split("\\s+")
        // dd/mm/yyyy 10:30
        if (tokens.length == 2) {
            time = tokens[1]
        }
        // dd/mm/yyyy 10:30 am
        else if (tokens.length == 3) {
            if ("am".equalsIgnoreCase(tokens[2])) {
                time = tokens[1]
            } else if ("pm".equalsIgnoreCase(tokens[2])) {
                String[] timeTokens = tokens[1].split(":")
                if (timeTokens.length > 1) {
                    int adjustedTime = Integer.parseInt(timeTokens[0]) + 12
                    time = "$adjustedTime:${timeTokens[1]}"
                } else {
                    time = timeTokens[0]
                }
            }
        } else {
            logger.info("No time field detected in [$title]")
        }

        return time
    }

    @Override
    String parseDate(String title, String name) {
        String date = ""

        String[] tokens = title.trim().split("\\s+")
        if (tokens.length > 0) {
            String dateString = tokens[0]
            String[] dateTokens = dateString.split("\\/")
            if (dateTokens.length == 3) {
                try {
                    int month = Integer.parseInt(dateTokens[0])
                    int day = Integer.parseInt(dateTokens[1])
                    int year
                    if (dateTokens[2].size() == 2) {
                        year = 2000 + Integer.parseInt(dateTokens[2])
                    } else {
                        year = Integer.parseInt(dateTokens[2])
                    }
                    date = formatDate(month, day, year)
                }
                catch (NumberFormatException e) {
                    logger.info("Invalid date format [$dateString]. Expected form MM/dd/yyyy")
                }
            }
        }

        if (date.isEmpty()) {
            logger.info("No date field found in [$title].. inspecting [$name]")
            date = parseDateFromFilename(name)
        }

        if (date.isEmpty()) {
            logger.info("No date field detected in [$title] or [$name]. Expected form MM/dd/yyyy")
        } else {
            logger.info("Found date [$date]")
        }

        return date
    }

    @Override
    String parseDateFromFilename(String filename) {
        def date = ''
        if (filename) {
            def dateString = filename[0..7]
            try {
                date = new Date().parse("yyyyMMdd", dateString).format("MM/dd/yyyy")
                return date
            }
            catch (ParseException e) {
                logger.warn("Invalid date format [$date]. Expected form yyyyMMdd")
                return date
            }
        }
        return date

    }

    @Override
    String formatDate(int month, int day, int year) {
        String date
        Calendar calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.MONTH, month - 1)
        calendar.set(Calendar.YEAR, year)

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy")
        date = dateFormat.format(calendar.getTime())
        return date
    }

    @Override
    String parseNotes(String comment) {
        if (comment == null || comment.isEmpty()) {
            return "";
        }
        return comment;
    }
}
