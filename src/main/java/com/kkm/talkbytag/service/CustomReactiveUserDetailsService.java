package com.kkm.talkbytag.service;

import com.kkm.talkbytag.domain.User;
import com.kkm.talkbytag.repository.UserRepository;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Service
public class CustomReactiveUserDetailsService implements ReactiveUserDetailsService {

    private final UserRepository userRepository;

    public CustomReactiveUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(user -> new User(user.getUsername(), user.getPassword(), Collections.emptyList()));
    }

    public Mono<User> createUser(User user){
        return userRepository.save(user);
    }

    public Mono<Boolean> changePassword(String username, String currentPassword, String newPassword) {
        return userRepository.findByUsername(username)
                .flatMap(user -> {
                    if (user.getPassword().equals(currentPassword)) {
                        user.setPassword(newPassword);
                        return userRepository.save(user)
                                .thenReturn(true);
                    } else {
                        return Mono.just(false);
                    }
                });
    }

    public Mono<Boolean> deleteUser(String username) {
        return userRepository.deleteByUsername(username)
                .thenReturn(true)
                .onErrorReturn(false);
    }
}
