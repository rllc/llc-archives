package com.rllc.spreadsheet.rest.domain

import javax.persistence.*

/**
 * Created by Steven McAdams on 6/10/15.
 */
@Entity
class Minister {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "minister_id")
    private long id;

    String firstName
    String middleName
    String lastName
}
