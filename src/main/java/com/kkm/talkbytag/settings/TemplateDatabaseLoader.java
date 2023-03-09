package com.kkm.talkbytag.settings;

import com.kkm.talkbytag.domain.Post;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TemplateDatabaseLoader {
    @Bean
    CommandLineRunner initialize(MongoOperations mongo) {
        return args -> {
            mongo.save(new Post( "hashTag1", "user1",  "contents1"));
            mongo.save(new Post( "hashTag2", "user1", "contents2"));
            mongo.save(new Post( "hashTag3", "user1",  "contents3"));
        };
    }
}
