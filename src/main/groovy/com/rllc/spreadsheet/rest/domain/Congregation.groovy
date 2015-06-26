package com.rllc.spreadsheet.rest.domain

import javax.persistence.*

/**
 * Created by Steven McAdams on 6/10/15.
 */
@Entity
class Congregation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = 'congregation_id')
    private long id

    String name
    String fullName

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "congregation", orphanRemoval = true)
    List<Sermon> sermons

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "congregation", cascade = CascadeType.ALL, orphanRemoval = true)
    AmazonCredentials amazonCredentials
}
