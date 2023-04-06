package com.kkm.talkbytag.repository;

import com.kkm.talkbytag.domain.Comment;
import com.kkm.talkbytag.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface PostRepository extends ReactiveCrudRepository<Post, String> {
    Flux<Post> findAllByPublished(Pageable pageable, boolean published);
    Flux<Post> findByHashTagContainingOrderByCreatedAtDesc(String hashTag);
    Flux<Post> findByContentsContainingOrderByCreatedAtDesc(String contents);
    Mono<Long> countByUsernameAndPublished(String username, boolean published);
}
