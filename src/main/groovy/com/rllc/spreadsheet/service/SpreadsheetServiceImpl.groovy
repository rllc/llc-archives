package com.rllc.spreadsheet.service

import com.google.gdata.data.spreadsheet.*
import com.google.gdata.util.ServiceException
import com.rllc.spreadsheet.domain.Sermon
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value

/**
 * Created by Steven McAdams on 4/25/15.
 */
class SpreadsheetServiceImpl implements SpreadsheetService {

    private static final Logger logger = LoggerFactory.getLogger(SpreadsheetServiceImpl.class);


    private static final String MINISTER = "minister";
    private static final String BIBLE_TEXT = "bibletext";
    private static final String DATE = "date";
    private static final String TIME = "time";

    @Value("\${google.username}")
    String username;
    @Value("\${google.password}")
    String password;
    @Autowired
    private Mp3DiscoveryService mp3DiscoveryService;
    @Value("\${google.spreadsheet}")
    private String googleSpreadsheet;
    @Value("\${aws.bucket}")
    private String awsBucket;

    @Override
    def updateSpreadsheet() throws IOException, ServiceException {
        updateSpreadsheet(mp3DiscoveryService.getMp3s());
    }

    def updateSpreadsheet(List<Sermon> sermons) throws ServiceException, IOException {

        logger.info("========= UPDATING SPREADSHEET =========");

        com.google.gdata.client.spreadsheet.SpreadsheetService service =
                new com.google.gdata.client.spreadsheet.SpreadsheetService("Application-v1");
        service.setUserCredentials(username, password);
        URL SPREADSHEET_FEED_URL = new URL(
                "https://spreadsheets.google.com/feeds/spreadsheets/private/full");
        SpreadsheetFeed feed = service.getFeed(SPREADSHEET_FEED_URL,
                SpreadsheetFeed.class);
        List<SpreadsheetEntry> spreadsheets = feed.getEntries();
        if (spreadsheets.size() == 0) {
            logger.info(googleSpreadsheet + " spreadsheet not found, exiting.");
            System.exit(1);
        }

        SpreadsheetEntry rllcSpreadsheet = null;
        for (SpreadsheetEntry spreadsheetEntry : spreadsheets) {
            if (googleSpreadsheet.equals(spreadsheetEntry.getTitle().getPlainText())) {
                rllcSpreadsheet = spreadsheetEntry;
            }
        }

        if (rllcSpreadsheet == null) {
            logger.info(googleSpreadsheet + " spreadsheet not found, exiting.");
            System.exit(1);
        }

        WorksheetFeed worksheetFeed = service.getFeed(
                rllcSpreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
        List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
        WorksheetEntry worksheet = worksheets.get(0);

        URL listFeedUrl = worksheet.getListFeedUrl();
        ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);

        for (Sermon sermon : sermons) {
            boolean alreadyExists = false;
            for (ListEntry row : listFeed.getEntries()) {
                if (row.getCustomElements().getValue("filelocation").contains(sermon.getFileName())) {
                    alreadyExists = true;
                    addMp3DataIfBlank(row, sermon);
                }
            }

            if (!alreadyExists) {
                logger.info("Creating a new entry in spreadsheet for [" + sermon.getFileName() + "]");

                // Create a local representation of the new row.
                ListEntry row = new ListEntry();
                row.getCustomElements().setValueLocal("minister", sermon.getMinister());
                row.getCustomElements().setValueLocal("bibletext", sermon.getBibleText());
                row.getCustomElements().setValueLocal("date", sermon.getDate());
                row.getCustomElements().setValueLocal("time", sermon.getTime());
                row.getCustomElements().setValueLocal("notes", sermon.getNotes());
                row.getCustomElements().setValueLocal("filelocation", awsBucket + sermon.getFileName());

                // Send the new row to the API for insertion.
                row = service.insert(listFeedUrl, row);
            } else {
                logger.info("file [{}] already exists.", sermon.getFileName());
            }
        }
    }

    private void addMp3DataIfBlank(ListEntry row, Sermon sermon) {
        boolean updateRequired = false;

        logger.info("> [{}] adding mp3 meta data if spreadsheet cells are blank", sermon.getFileName());

        if (row.customElements.getValue(MINISTER)?.isEmpty() &&
                !sermon.minister?.isEmpty()) {
            logger.info("> setting {} to {}", MINISTER, sermon.getMinister());
            row.getCustomElements().setValueLocal(MINISTER, sermon.getMinister());
            updateRequired = true;
        }

        if (row.customElements.getValue(BIBLE_TEXT)?.isEmpty() &&
                !sermon.bibleText?.isEmpty()) {
            logger.info("> setting {} to {}", BIBLE_TEXT, sermon.getBibleText());
            row.getCustomElements().setValueLocal(BIBLE_TEXT, sermon.getBibleText());
            updateRequired = true;
        }

        if (row.customElements.getValue(DATE)?.isEmpty() &&
                !sermon.getDate()?.isEmpty()) {
            logger.info("> setting {} to {}", DATE, sermon.getDate());
            row.getCustomElements().setValueLocal(DATE, sermon.getDate());
            updateRequired = true;
        }

        if (row.customElements.getValue(TIME)?.isEmpty() &&
                !sermon.getTime()?.isEmpty()) {
            logger.info("> setting {} to {}", TIME, sermon.getTime());
            row.getCustomElements().setValueLocal(TIME, sermon.getTime());
            updateRequired = true;
        }

        if (updateRequired) {
            try {
                logger.info("> sending update request!");
                row.update();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        } else {
            logger.info("> no updates required.");
        }
    }
}
