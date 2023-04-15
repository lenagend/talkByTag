package com.kkm.talkbytag.repository;

import com.kkm.talkbytag.domain.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, String> {
    Mono<User> findByUsername(String username);
}
