package com.kkm.talkbytag.repository;

import com.kkm.talkbytag.domain.Post;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PostRepository extends ReactiveCrudRepository<Post, String> {
}
