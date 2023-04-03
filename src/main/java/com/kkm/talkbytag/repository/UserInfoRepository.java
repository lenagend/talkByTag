package com.kkm.talkbytag.repository;

import com.kkm.talkbytag.domain.User;
import com.kkm.talkbytag.domain.UserInfo;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserInfoRepository extends ReactiveCrudRepository<UserInfo, Long> {
    Mono<UserInfo> findByUsername(String username);

}
