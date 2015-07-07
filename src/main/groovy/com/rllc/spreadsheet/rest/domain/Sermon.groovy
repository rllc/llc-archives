package com.rllc.spreadsheet.rest.domain

import com.fasterxml.jackson.annotation.JsonPropertyOrder

import javax.persistence.*

/**
 * Created by Steven McAdams on 6/10/15.
 */
@Entity
@JsonPropertyOrder(value = ["id", "congregation", "minister", "bibleText", "comments"])
class Sermon implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "sermon_id")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "congregation_id")
    Congregation congregation

    Date date

    String minister
    String bibleText
    String comments

    @Column(unique = true, nullable = false)
    String fileUrl

}
