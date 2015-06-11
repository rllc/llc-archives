package com.rllc.spreadsheet

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

/**
 * Created by Steven McAdams on 3/7/15.
 */

@Configuration
@PropertySource("classpath:spreadsheet.properties")
public class CoreConfiguration {

//    @Bean(name="objectMapper")
//    @Primary
//    public ObjectMapper createObjectMapper()
//    {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//        mapper.configure(SerializationFeature.INDENT_OUTPUT,false);
//        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//
//        return mapper;
//    }
}