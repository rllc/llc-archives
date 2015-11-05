package org.llc.archive.rest.domain

import javax.persistence.*

/**
 * Created by Steven McAdams on 6/10/15.
 */
@Entity
class Minister {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "minister_id")
    Long id

    String firstName
    String middleName
    String lastName

    def getFullName() {
        return "$lastName, $firstName, $middleName"
    }

    def getNaturalName() {
        "$firstName $lastName"
    }
}
