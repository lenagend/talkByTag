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
            mongo.save(new Post( "hashTag1", "user1", "title1", "contents1", LocalDateTime.of(2023, 3, 8, 20, 24, 0)));
            mongo.save(new Post( "hashTag2", "user1", "title2", "contents2", LocalDateTime.of(2023, 3, 8, 20, 24, 0)));
            mongo.save(new Post( "hashTag3", "user1", "title3", "contents3", LocalDateTime.of(2023, 3, 8, 20, 24, 0)));
            mongo.save(new Post( "hashTag4", "user1", "title4", "contents4", LocalDateTime.of(2023, 3, 8, 20, 24, 0)));
            mongo.save(new Post( "hashTag5", "user1", "title5", "contents5", LocalDateTime.of(2023, 3, 8, 20, 24, 0)));
        };
    }
}
