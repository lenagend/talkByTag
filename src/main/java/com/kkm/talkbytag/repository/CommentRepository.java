package com.kkm.talkbytag.repository;

import com.kkm.talkbytag.domain.Comment;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CommentRepository extends ReactiveCrudRepository<Comment, String> {
    Flux<Comment> findByPostIdOrderByCreatedAtDesc(String postId);
    Mono<Long> countByPostIdAndPublished(String postId, boolean published);
}
