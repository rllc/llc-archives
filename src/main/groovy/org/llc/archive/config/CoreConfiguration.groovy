package org.llc.archive.config

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
            String username = 'docker';
            String password = 'docker';
            String url = "jdbc:postgresql://db:5432/llc";
            String dbProperty = System.getProperty("database.url")
            logger.info "dbProperty : $dbProperty"


            if (dbProperty) {
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