package com.rllc.spreadsheet.rest.domain

import com.fasterxml.jackson.annotation.JsonIgnore

import javax.persistence.*

/**
 * Created by Steven McAdams on 5/5/15.
 */
@Entity
class AmazonCredentials implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "amazon_credentials_id")
    private long id;

    @OneToOne
    @PrimaryKeyJoinColumn()
    private Congregation congregation


    @JsonIgnore
    String accessKey
    @JsonIgnore
    String secretKey

    String bucket

}