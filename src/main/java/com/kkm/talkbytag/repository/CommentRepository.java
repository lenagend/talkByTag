package com.kkm.talkbytag.repository;

import com.kkm.talkbytag.domain.Comment;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CommentRepository extends ReactiveCrudRepository<Comment, String> {
    Flux<Comment> findByPostIdAndUpperCommentIdIsNullOrderByCreatedAtDesc(String postId);
    Flux<Comment> findByUpperCommentIdOrderByCreatedAtDesc(String upperCommentId);
    Mono<Long> countByPostIdAndPublished(String postId, boolean published);
    Mono<Long> countByUsernameAndPublished(String username, boolean published);
}
