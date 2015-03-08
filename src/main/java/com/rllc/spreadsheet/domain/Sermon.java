package com.rllc.spreadsheet.domain;

/**
 * Created by Steven McAdams on 3/7/15.
 */
public class Sermon {

    private String date;
    private String time;
    private String minister;
    private String bibleText;
    private String notes;
    private String fileName;

    public Sermon(){
        date = "";
        time = "";
        minister = "";
        bibleText = "";
        notes = "";
        fileName = "";
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMinister() {
        return minister;
    }

    public void setMinister(String minister) {
        this.minister = minister;
    }

    public String getBibleText() {
        return bibleText;
    }

    public void setBibleText(String bibleText) {
        this.bibleText = bibleText;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
