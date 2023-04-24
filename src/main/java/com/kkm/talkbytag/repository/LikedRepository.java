package com.kkm.talkbytag.repository;

import com.kkm.talkbytag.domain.Liked;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LikedRepository  extends ReactiveCrudRepository<Liked, String> {
    Mono<Liked> findByUsernameAndPostId(String username, String postId);
    Mono<Liked> findByUsernameAndCommentId(String username, String commentId);
    Mono<Boolean> existsByPostIdAndUsername(String postId, String username);
    Mono<Long> countByPostId(String postId);
    Mono<Boolean> existsByCommentIdAndUsername(String commentId, String username);
    Mono<Long> countByCommentId(String commentId);
    Mono<Long> countByUsername(String username);
    Flux<Liked> findByUsername(String username);
}
