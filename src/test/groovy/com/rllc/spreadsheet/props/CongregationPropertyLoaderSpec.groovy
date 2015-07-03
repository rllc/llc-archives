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

        env.withProperty('llc.rockford.password', 'rllc-password')
        env.withProperty('llc.rockford.aws.bucket', 'aws-bucket')
        env.withProperty('llc.rockford.aws.accessKey', 'aws-accessKey')
        env.withProperty('llc.rockford.aws.secretKey', 'aws-secretKey')
        env.withProperty('llc.minneapolis.password', 'mllc-password')
        env.withProperty('llc.minneapolis.aws.bucket', 'mllc-aws-bucket')
        env.withProperty('llc.minneapolis.aws.accessKey', 'mllc-aws-accessKey')
        env.withProperty('llc.minneapolis.aws.secretKey', 'mllc-aws-secretKey')

        congregationPropertyLoader = new CongregationPropertyLoader()
        congregationPropertyLoader.env = env
    }


    def "test properties are loaded on startup"() {
        when: "spring application context is initialized"
        congregationPropertyLoader.init()

        then: "configuration for multiple credentials are loaded"
        congregationPropertyLoader.credentials.size() == 2

        when: "rockford properties are retrieved"
        def rockford = congregationPropertyLoader.credentials['rockford']

        then: "rockford properties are loaded"
        rockford.password == 'rllc-password'
        rockford.amazonCredentials.bucket == 'aws-bucket'
        rockford.amazonCredentials.accessKey == 'aws-accessKey'
        rockford.amazonCredentials.secretKey == 'aws-secretKey'

        when: "minneapolis properties are retrieved"
        def minneapolis = congregationPropertyLoader.credentials['minneapolis']

        then: "minneapolis properties are loaded"
        minneapolis.password == 'mllc-password'
        minneapolis.amazonCredentials.bucket == 'mllc-aws-bucket'
        minneapolis.amazonCredentials.accessKey == 'mllc-aws-accessKey'
        minneapolis.amazonCredentials.secretKey == 'mllc-aws-secretKey'

    }


}
