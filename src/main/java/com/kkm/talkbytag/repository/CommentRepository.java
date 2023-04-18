package com.kkm.talkbytag.repository;

import com.kkm.talkbytag.domain.Comment;
import com.kkm.talkbytag.domain.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CommentRepository extends ReactiveCrudRepository<Comment, String> {
    Flux<Comment> findByPostIdAndUpperCommentIdIsNullOrderByCreatedAtDesc(String postId);
    Flux<Comment> findByUpperCommentIdOrderByCreatedAtDesc(String upperCommentId);
    Mono<Long> countByPostIdAndPublished(String postId, boolean published);
    Mono<Long> countByUsernameAndPublished(String username, boolean published);
    Flux<Comment> findByUsernameAndPublished(String username, boolean published);
    Flux<Comment> findByUsernameAndPublished(Pageable pageable, String username, boolean published);
}
