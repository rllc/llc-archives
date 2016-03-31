package org.llc.archive.props

import org.llc.archive.domain.AmazonCredentials
import org.llc.archive.domain.CongregationCredentials
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

    Map<String, CongregationCredentials> credentials = [:]

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
        props.groupBy { String prop -> prop.split("\\.")[1] }.each { congregation, value ->
            credentials[congregation] = new CongregationCredentials(
                    password: env.getProperty("llc.${congregation}.password"),
                    amazonCredentials: new AmazonCredentials(
                            accessKey: env.getProperty("llc.${congregation}.aws.accessKey"),
                            secretKey: env.getProperty("llc.${congregation}.aws.secretKey"),
                            bucket: env.getProperty("llc.${congregation}.aws.bucket")
                    )
            )
        }

        logger.info "found credentials : ${credentials.collect { it.key }}"
    }

}
