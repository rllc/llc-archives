package com.rllc.spreadsheet.props

import org.springframework.core.env.Environment
import org.springframework.mock.env.MockEnvironment
import spock.lang.Specification

/**
 * Created by Steven McAdams on 5/10/15.
 */

class CongregationPropertyLoaderSpec extends Specification {

    CongregationPropertyLoader congregationPropertyLoader

    Environment env

    def setup() {
        env = new MockEnvironment()

        env.withProperty('llc.rockford.shortName', 'RLLC')
        env.withProperty('llc.rockford.longName', 'Rockford Laestadian Lutheran Church')
        env.withProperty('llc.rockford.aws.username', 'aws-username')
        env.withProperty('llc.rockford.aws.bucket', 'aws-bucket')
        env.withProperty('llc.rockford.aws.key.id', 'aws-accessKey')
        env.withProperty('llc.rockford.aws.key.token', 'aws-secretKey')
        env.withProperty('llc.rockford.google.username', 'google-username')
        env.withProperty('llc.rockford.google.password', 'google-password')
        env.withProperty('llc.rockford.google.spreadsheet', 'google-spreadsheet')
        env.withProperty('llc.rockford.google.worksheet', 'google-worksheet')
        env.withProperty('llc.minneapolis.shortName', 'MLLC')
        env.withProperty('llc.minneapolis.longName', 'Minneapolis Laestadian Lutheran Church')
        env.withProperty('llc.minneapolis.aws.username', 'mllc-aws-username')
        env.withProperty('llc.minneapolis.aws.bucket', 'mllc-aws-bucket')
        env.withProperty('llc.minneapolis.aws.key.id', 'mllc-aws-accessKey')
        env.withProperty('llc.minneapolis.aws.key.token', 'mllc-aws-secretKey')
        env.withProperty('llc.minneapolis.google.username', 'mllc-google-username')
        env.withProperty('llc.minneapolis.google.password', 'mllc-google-password')
        env.withProperty('llc.minneapolis.google.spreadsheet', 'mllc-google-spreadsheet')
        env.withProperty('llc.minneapolis.google.worksheet', 'mllc-google-worksheet')

        congregationPropertyLoader = new CongregationPropertyLoader()
        congregationPropertyLoader.env = env
    }


    def "test properties are loaded on startup"() {
        when: "spring application context is initialized"
        congregationPropertyLoader.init()

        then: "configuration for multiple congregations are loaded"
        congregationPropertyLoader.congregations.size() == 2

        when: "rockford properties are retrieved"
        def rockford = congregationPropertyLoader.congregations['rockford']

        then: "rockford properties are loaded"
        rockford.shortName == 'RLLC'
        rockford.longName == 'Rockford Laestadian Lutheran Church'
        rockford.awsCredentials.bucket == 'aws-bucket'
        rockford.awsCredentials.accessKey == 'aws-accessKey'
        rockford.awsCredentials.secretKey == 'aws-secretKey'
        rockford.googleCredentials.username == 'google-username'
        rockford.googleCredentials.password == 'google-password'
        rockford.googleCredentials.spreadsheet == 'google-spreadsheet'
        rockford.googleCredentials.worksheet == 'google-worksheet'

        when: "minneapolis properties are retrieved"
        def minneapolis = congregationPropertyLoader.congregations['minneapolis']

        then: "minneapolis properties are loaded"
        minneapolis.shortName == 'MLLC'
        minneapolis.longName == 'Minneapolis Laestadian Lutheran Church'
        minneapolis.awsCredentials.bucket == 'mllc-aws-bucket'
        minneapolis.awsCredentials.accessKey == 'mllc-aws-accessKey'
        minneapolis.awsCredentials.secretKey == 'mllc-aws-secretKey'
        minneapolis.googleCredentials.username == 'mllc-google-username'
        minneapolis.googleCredentials.password == 'mllc-google-password'
        minneapolis.googleCredentials.spreadsheet == 'mllc-google-spreadsheet'
        minneapolis.googleCredentials.worksheet == 'mllc-google-worksheet'

    }


}
