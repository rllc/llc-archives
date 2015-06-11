package com.rllc.spreadsheet.rest.domain

import javax.persistence.*

/**
 * Created by Steven McAdams on 6/10/15.
 */
@Entity
class Sermon {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "sermon_id")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "congregation_id", nullable = false)
    Congregation congregation

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "minister_id", nullable = false)
    Minister minister
    Date date

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bible_text_id", nullable = false)
    BibleText bibleText

    String comments

}
