package org.llc.archive.config

import org.springframework.boot.autoconfigure.data.rest.SpringBootRepositoryRestMvcConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.data.rest.core.config.RepositoryRestConfiguration
import org.springframework.http.MediaType

/**
 * Created by Steven McAdams on 6/11/15.
 */
@Configuration
public class RestConfiguration extends SpringBootRepositoryRestMvcConfiguration {

    @Override
    protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.defaultMediaType = MediaType.APPLICATION_JSON
        config.baseUri = URI.create('/api')
    }
}
