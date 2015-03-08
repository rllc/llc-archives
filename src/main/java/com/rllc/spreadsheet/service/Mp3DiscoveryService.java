package com.rllc.spreadsheet.service;

import com.mpatric.mp3agic.*;
import com.rllc.spreadsheet.domain.Sermon;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Steven McAdams on 3/7/15.
 */
@Component
public class Mp3DiscoveryService {

    private static final Logger logger = LoggerFactory.getLogger(Mp3DiscoveryService.class);

    @Value("${mp3.directory}")
    private String mp3Directory;

    @PostConstruct
    public void init() {
        logger.info("scanning mp3 directory [{}]", mp3Directory);
    }

    public List<Sermon> getMp3s() {
        List<Sermon> sermonList = new ArrayList<>();
        List<File> mp3Files = (List<File>) FileUtils.listFiles(new File(mp3Directory), new String[]{"mp3"}, true);

        for (File mp3FileHandle : mp3Files) {
            logger.info("-------------- {}  --------------", mp3FileHandle.getName());
            try {
                Mp3File mp3file = new Mp3File(mp3FileHandle.getAbsolutePath());

                String filename = "";
                String date = "";
                String time = "";
                String bibleText = "";
                String minister = "";

                if (mp3file.hasId3v1Tag()) {
                    ID3v1 id3v1Tag = mp3file.getId3v1Tag();
                    filename = mp3FileHandle.getAbsolutePath()
                            .replace(mp3Directory, "")
                            .replace("\\", "/");
                    if (filename.startsWith("/")) {
                        filename = filename.substring(1);
                    }
                    date = parseDate(id3v1Tag.getTitle(), mp3FileHandle.getName());
                    time = parseTime(id3v1Tag.getTitle());
                    bibleText = parseBibleText(id3v1Tag.getAlbum());
                    minister = parseMinister(id3v1Tag.getArtist());
                } else if (mp3file.hasId3v2Tag()) {
                    ID3v2 id3v2Tag = mp3file.getId3v2Tag();
                    filename = mp3FileHandle.getAbsolutePath()
                            .replace(mp3Directory, "")
                            .replace("\\", "/");
                    if (filename.startsWith("/")) {
                        filename = filename.substring(1);
                    }
                    date = parseDate(id3v2Tag.getTitle(), mp3FileHandle.getName());
                    time = parseTime(id3v2Tag.getTitle());
                    bibleText = parseBibleText(id3v2Tag.getAlbum());
                    minister = parseMinister(id3v2Tag.getArtist());
                }

                logger.info("filename : " + filename);
                logger.info("date : " + date);
                logger.info("time : " + time);
                logger.info("bibleText : " + bibleText);
                logger.info("minister : " + minister);

                Sermon sermon = new Sermon();
                sermon.setFileName(filename);
                sermon.setDate(date);
                sermon.setTime(time);
                sermon.setBibleText(bibleText);
                sermon.setMinister(minister);
                sermonList.add(sermon);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (UnsupportedTagException e) {
                e.printStackTrace();
            } catch (InvalidDataException e) {
                e.printStackTrace();
            }
        }
        return sermonList;
    }

    private String parseMinister(String artist) {
        String[] tokens = artist.split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String token : tokens) {
            sb.append(StringUtils.capitalize(token)).append(" ");
        }
        return sb.toString().trim();
    }

    private String parseBibleText(String album) {
        return album != null ? album : "";
    }

    private String parseTime(String title) {
        String time = "";
        title = title.replaceAll("[AaMm]+", "");
        String[] tokens = title.split("\\s+");
        // dd/mm/yyyy 10:30
        if (tokens.length == 2) {
            time = tokens[1];
        }
        // dd/mm/yyyy 10:30 am
        else if (tokens.length == 3) {
            if ("am".equalsIgnoreCase(tokens[2])) {
                time = tokens[1];
            } else if ("pm".equalsIgnoreCase(tokens[2])) {
                String[] timeTokens = tokens[1].split(":");
                if (timeTokens.length > 1) {
                    int adjustedTime = Integer.parseInt(timeTokens[0]) + 12;
                    time = adjustedTime + timeTokens[1];
                } else {
                    time = timeTokens[0];
                }
            }
        } else {
            logger.info("No time field detected in [" + title + "]");
        }

        return time;
    }

    private String parseDate(String title, String name) {
        String date = "";

        String[] tokens = title.split("\\s+");
        if (tokens.length > 0) {
            String dateString = tokens[0];
            String[] dateTokens = dateString.split("\\/");
            if (dateTokens.length == 3) {
                int month = Integer.parseInt(dateTokens[0]) - 1;
                int day = Integer.parseInt(dateTokens[1]);
                int year = Integer.parseInt(dateTokens[2]);

                date = formatDate(month, day, year);
            } else {
                logger.info("No date field detected in [" + title + "]");
                if (name.length() > 8) {
                    int year = Integer.parseInt(name.substring(0, 4));
                    int month = Integer.parseInt(name.substring(4, 6)) - 1;
                    int day = Integer.parseInt(name.substring(6, 7));

                    date = formatDate(month, day, year);
                } else {
                    logger.info("No date field detected in [" + name + "]");
                }
            }

        }

        return date;
    }

    private String formatDate(int month, int day, int year) {
        String date;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        date = dateFormat.format(calendar.getTime());
        return date;
    }
}
