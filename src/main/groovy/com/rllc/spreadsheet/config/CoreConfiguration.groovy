package com.rllc.spreadsheet.config

import org.apache.commons.dbcp2.BasicDataSource
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

import javax.sql.DataSource

/**
 * Created by Steven McAdams on 3/7/15.
 */

@Configuration
@PropertySource("classpath:spreadsheet.properties")
public class CoreConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(CoreConfiguration.class);

    @Bean
    public DataSource dataSource() {
        logger.info "loading datasource..."

        URI dbUri;
        try {
            String username = "username";
            String password = "password";
            String url = "jdbc:postgresql://localhost/llc";
            String dbProperty = System.getProperty("DATABASE_URL");
            logger.info "dbProperty : $dbProperty"


            if (dbProperty != null) {
                dbUri = new URI(dbProperty);

                username = dbUri.userInfo.split(":")[0];
                password = dbUri.userInfo.split(":")[1];
                url = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
            }

            BasicDataSource basicDataSource = new BasicDataSource();
            basicDataSource.url = url
            basicDataSource.username = username
            basicDataSource.password = password
            return basicDataSource;

        } catch (URISyntaxException e) {
            //Deal with errors here.
            e.printStackTrace()
        }
    }

}