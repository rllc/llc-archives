package org.llc.archive.config

import org.llc.archive.rest.domain.Congregation
import org.llc.archive.rest.domain.Sermon
import org.springframework.context.annotation.Configuration
import org.springframework.data.rest.core.config.RepositoryRestConfiguration
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter
import org.springframework.http.MediaType

/**
 * Created by Steven McAdams on 6/11/15.
 */
@Configuration
public class RestConfiguration extends RepositoryRestConfigurerAdapter {

    @Override
    void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.defaultMediaType = MediaType.APPLICATION_JSON
        config.exposeIdsFor(Sermon.class);
        config.exposeIdsFor(Congregation.class);
        config.baseUri = URI.create('/api')
    }
}
