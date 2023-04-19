package com.kkm.talkbytag.repository;

import com.kkm.talkbytag.domain.Liked;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface LikedRepository  extends ReactiveCrudRepository<Liked, String> {
    Mono<Liked> findByUsernameAndPostId(String username, String postId);
    Mono<Liked> findByUsernameAndCommentId(String username, String commentId);
    Mono<Boolean> existsByPostIdAndUsername(String postId, String username);
    Mono<Long> countByPostId(String postId);
    Mono<Boolean> existsByCommentIdAndUsername(String commentId, String username);
    Mono<Long> countByCommentId(String commentId);
    Mono<Long> countByUsername(String username);
    Flux<Liked> findByUsername(String username);
    @Query(value = "{'createdAt': {$gte: ?0, $lte: ?1}}", fields = "{'postId': 1}", sort = "{'createdAt': -1}")
    Flux<Liked> findTopLikedPostsByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
