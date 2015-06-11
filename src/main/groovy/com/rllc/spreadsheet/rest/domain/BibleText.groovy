package com.rllc.spreadsheet.rest.domain

import javax.persistence.*

/**
 * Created by Steven McAdams on 6/10/15.
 */
@Entity
class BibleText {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "bible_text_id")
    private long id;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "bibleText", orphanRemoval = false)
    List<Book> book
    Integer chapter
    Integer startVerse
    Integer endVerse

}
