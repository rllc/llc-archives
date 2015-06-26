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
    @JoinColumn(name = "congregation_id", nullable = false)
    Congregation congregation

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "minister_id", nullable = true)
    Minister minister
    Date date

    String bibleText
    String comments

    @Column(unique = true, nullable = false)
    String fileUrl

}
