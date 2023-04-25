package com.kkm.talkbytag.Configuration;

import com.kkm.talkbytag.domain.Post;
import com.kkm.talkbytag.domain.UserInfo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;

@Component
public class TemplateDatabaseLoader {
    @Bean
    CommandLineRunner initialize(MongoOperations mongo) {
        return args -> {
            LocalDateTime baseDate = LocalDateTime.of(2023, Month.APRIL, 24, 0, 0);

            for (int i = 0; i < 100; i++){
                LocalDateTime createdAt = baseDate.plusMinutes(i * 10);
                LocalDateTime modifiedAt = createdAt;
                mongo.save(new Post("" + i, "제목" + i, "testuser1@gmail.com", "testContents" + i, createdAt, modifiedAt, true, 0));
            }
            mongo.save(new User("testuser1@gmail.com", "password1", Collections.emptyList()));
            mongo.save(new UserInfo("userInfo1", "testuser1@gmail.com", "운영자", "/images/avatar/master.jpg", null, null, null, null));

        };
    }
}
