package com.sas.dhop.site.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EntityScan({"com.sas.dhop.site.model", "com.sas.dhop.site.model.nosql"})
@EnableJpaRepositories(basePackages = "com.sas.dhop.site.repository")
@EnableMongoRepositories(basePackages = "com.sas.dhop.site.repository.nosql")
public class DataSourceConfiguration {

    @Bean
    public MongoTemplate mongoTemplate(MongoDatabaseFactory factory, MappingMongoConverter converter) {
        return new MongoTemplate(factory, converter);
    }
}
