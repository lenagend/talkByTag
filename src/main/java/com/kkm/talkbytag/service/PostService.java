package com.kkm.talkbytag.service;

import com.kkm.talkbytag.domain.Comment;
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
    public Flux<Post> getPosts(){return this.postRepository.findAllByOrderByCreatedAtDesc();}

    public Mono<Post> savePost(Post post){return this.postRepository.save(post);}

    public Mono<Post> getPostByPostId(String postId){return this.postRepository.findById(postId);}

    public Flux<Comment> getCommentsByPostId(String postId) {return postRepository.findCommentByPostId(postId);}

    public Mono<Comment> createComment(String postId, Comment comment){
        return postRepository.findById(postId)
                .flatMap(p -> {
                    p.getComments().add(comment);
                    return postRepository.save(p).thenReturn(comment);
                });
    }
}
