package com.kkm.talkbytag.Configuration;

import com.kkm.talkbytag.domain.Post;
import com.kkm.talkbytag.domain.UserInfo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;

@Component
public class TemplateDatabaseLoader {
    @Bean
    CommandLineRunner initialize(MongoOperations mongo) {
        return args -> {
            for (int i = 0; i < 100; i++){
                mongo.save(new Post("" + i, "제목" + i, "testuser1@gmail.com", "testContents" + i, LocalDateTime.now(), LocalDateTime.now(), true, 0));
                Thread.sleep(10); // 0.1초 지연
            }
            mongo.save(new User("testuser1@gmail.com", "password1", Collections.emptyList()));
            mongo.save(new UserInfo("userInfo1", "testuser1@gmail.com", "운영자", "/images/avatar/master.jpg", null, null, null, null));

        };
    }
}
