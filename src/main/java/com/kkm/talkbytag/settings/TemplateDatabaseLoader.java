package com.kkm.talkbytag.settings;

import com.kkm.talkbytag.domain.Post;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

@Component
public class TemplateDatabaseLoader {
    @Bean
    CommandLineRunner initialize(MongoOperations mongo) {
        return args -> {
            mongo.save(new Post( "hashTag1", "user1", "title1", "contents1"));
        };
    }
}
