package com.kkm.talkbytag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
public class TalkByTagApplication {

    public static void main(String[] args) {
        SpringApplication.run(TalkByTagApplication.class, args);
    }

}
