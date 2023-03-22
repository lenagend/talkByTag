package com.kkm.talkbytag.service;

import com.kkm.talkbytag.domain.Comment;
import com.kkm.talkbytag.domain.Post;
import com.kkm.talkbytag.repository.CommentRepository;
import com.kkm.talkbytag.repository.PostRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PostService {

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;

    public PostService(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public Flux<Post> getPosts(){return this.postRepository.findAllByOrderByCreatedAtDesc();}

    public Mono<Post> savePost(Post post){return this.postRepository.save(post);}

    public Mono<Post> getPostByPostId(String postId){return this.postRepository.findById(postId);}

    public Flux<Post> searchByHashTag(String hashTag){
        return postRepository.findByHashTagContaining(hashTag);
    }

    public Mono<Comment> getCommentById(String id){return this.commentRepository.findById(id);}

    public Flux<Comment> getCommentsByPostId(String postId){return this.commentRepository.findByPostIdOrderByCreatedAtDesc(postId);}

    public Mono<Comment> saveComment(Comment comment){return this.commentRepository.save(comment);}

    public Mono<Long> getCommentCount(String postId, boolean published){return this.commentRepository.countByPostIdAndPublished(postId, published);}

    public Flux<Comment> getCommentsByUpperCommentId(String upperCommentId){return this.commentRepository.findByUpperCommentIdOrderByCreatedAtDesc(upperCommentId);}

}
