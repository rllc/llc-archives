package org.llc.archive.rest.domain

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
    Long id

    Date date
    String username
    Long executionTimeMs

}
