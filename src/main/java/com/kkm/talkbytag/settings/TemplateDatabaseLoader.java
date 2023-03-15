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
            for (int i = 0; i < 100; i++){
                mongo.save(new Post( "freeTalk", "user1",  "contents1"));

            }
        };
    }
}
