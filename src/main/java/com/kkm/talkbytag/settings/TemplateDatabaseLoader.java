package com.kkm.talkbytag.settings;

import com.kkm.talkbytag.domain.Comment;
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
            mongo.save(new Post( "post1","freeTalk", "user1",  "contents1"));
            mongo.save(new Comment( "post1", "contents1",  "testuser1"));
        };
    }
}
