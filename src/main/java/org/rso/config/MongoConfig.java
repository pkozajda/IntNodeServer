package org.rso.config;

import com.github.fakemongo.Fongo;
import com.mongodb.Mongo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

/**
 * Created by Radosław on 24.05.2016.
 */
@Configuration
public class MongoConfig extends AbstractMongoConfiguration {
    @Override
    protected String getDatabaseName() {
        return "rsoDB";
    }

    @Bean
    public Mongo mongo() throws Exception {
        return new Fongo(getDatabaseName()).getMongo();
//        return new Mongo();
    }

//    @Bean
//    public MongoTemplate mongoTemplate() throws Exception {
//        return new MongoTemplate(mongo(),getDatabaseName());
//    }
}
