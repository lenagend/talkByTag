package com.kkm.talkbytag.repository;

import com.kkm.talkbytag.domain.Comment;
import com.kkm.talkbytag.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;


public interface PostRepository extends ReactiveCrudRepository<Post, String> {
    Flux<Post> findAllByOrderByCreatedAtDesc();
    Flux<Comment> findCommentByPostId(String postId);
}
