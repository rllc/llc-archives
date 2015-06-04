package com.rllc.spreadsheet.props

import com.rllc.spreadsheet.domain.AmazonCredentials
import com.rllc.spreadsheet.domain.Congregation
import com.rllc.spreadsheet.domain.GoogleCredentials
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.AbstractEnvironment
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

/**
 * Created by Steven McAdams on 5/5/15.
 */
@Component
class CongregationPropertyLoader {

    private static final Logger logger = LoggerFactory.getLogger(CongregationPropertyLoader.class)

    @Autowired
    Environment env;

    Map<String, Congregation> congregations = [:]

    @PostConstruct
    def init() {
        logger.info "Scanning properties for llc.* properties"
        def props = []
        ((AbstractEnvironment) env).propertySources.each { propertySource ->
            propertySource.properties.each { key, value ->
                if ('propertyNames'.equals(key)) {
                    value.each { propName ->
                        if (propName.toString().startsWith('llc')) {
                            props << propName
                        }
                    }
                }
            }

        }
        props.groupBy { prop -> prop.split("\\.")[1] }.each { congregation, value ->
            congregations[congregation] = new Congregation(
                    shortName: env.getProperty("llc.${congregation}.shortName"),
                    longName: env.getProperty("llc.${congregation}.longName"),
                    mp3Directory: env.getProperty("llc.${congregation}.mp3Directory"),
                    googleCredentials: new GoogleCredentials(
                            username: env.getProperty("llc.${congregation}.google.username"),
                            password: env.getProperty("llc.${congregation}.google.password"),
                            spreadsheet: env.getProperty("llc.${congregation}.google.spreadsheet"),
                            worksheet: env.getProperty("llc.${congregation}.google.worksheet")
                    ),
                    awsCredentials: new AmazonCredentials(
                            accessKey: env.getProperty("llc.${congregation}.aws.accessKey"),
                            secretKey: env.getProperty("llc.${congregation}.aws.secretKey"),
                            bucket: env.getProperty("llc.${congregation}.aws.bucket")
                    )
            )
        }

        logger.info "found congregations : ${congregations.collect { it.key }}"
    }

}
