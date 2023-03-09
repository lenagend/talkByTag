package com.kkm.talkbytag.service;

import com.kkm.talkbytag.domain.Post;
import com.kkm.talkbytag.repository.PostRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Flux<Post> getPosts(){return this.postRepository.findAll();}

    public Mono<Post> savePost(Post post){return this.postRepository.save(post);}
}
