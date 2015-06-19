package com.rllc.spreadsheet.rest.domain

import javax.persistence.*

/**
 * Created by Steven McAdams on 6/10/15.
 */
@Entity
class BibleText implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "bible_text_id")
    private long id;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "bibleText", orphanRemoval = false)
    List<Book> book
    Integer chapter
    Integer start
    Integer end

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sermon_id", nullable = false)
    Sermon sermon

}
