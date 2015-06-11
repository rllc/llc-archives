package com.rllc.spreadsheet.rest.domain

import javax.persistence.*

/**
 * Created by Steven McAdams on 6/10/15.
 */
@Entity
class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "book_id")
    private long id;

    String name
    String abbreviation
    String testament

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bible_text_id", nullable = true)
    private BibleText bibleText;
}
