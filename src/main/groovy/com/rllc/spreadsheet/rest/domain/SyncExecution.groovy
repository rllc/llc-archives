package com.rllc.spreadsheet.rest.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

/**
 * Created by Steven McAdams on 7/31/15.
 */
@Entity
class SyncExecution {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id

    Date date
    String username
    Long executionTimeMs

}
