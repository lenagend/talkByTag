package com.kkm.talkbytag.repository;

import com.kkm.talkbytag.domain.Comment;
import com.kkm.talkbytag.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;


public interface PostRepository extends ReactiveCrudRepository<Post, String> {
    Flux<Post> findAllByPublishedOrderByCreatedAtDesc(Pageable pageable, boolean published);
    Flux<Post> findByTitleContainingAndPublished(Pageable pageable, String title, boolean published);
    Flux<Post> findByContentsContainingAndPublished(Pageable pageable, String contents, boolean published);
    Mono<Long> countByUsernameAndPublished(String username, boolean published);
    Flux<Post> findByUsernameAndPublished(String username, boolean published);
    Flux<Post> findByIdAndPublished(Pageable pageable, String id, boolean published);
    Flux<Post> findByUsernameAndPublished(Pageable pageable,  String username, boolean published);
    Flux<Post> findByCreatedAtBetweenAndPublishedOrderByLikesDescCreatedAtDesc(LocalDateTime start, LocalDateTime end, boolean published, Pageable pageable);
    Mono<Long> countByPublished(boolean published);
    Mono<Long> countByCreatedAtBetweenAndPublished(LocalDateTime start, LocalDateTime end, boolean published);

}
