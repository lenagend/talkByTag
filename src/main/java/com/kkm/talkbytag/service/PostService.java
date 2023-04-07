package com.kkm.talkbytag.service;

import com.kkm.talkbytag.domain.Comment;
import com.kkm.talkbytag.domain.Post;
import com.kkm.talkbytag.repository.CommentRepository;
import com.kkm.talkbytag.repository.PostRepository;
import org.springframework.data.domain.Pageable;
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

    public Flux<Post> getPosts(Pageable pageable, boolean published){return this.postRepository.findAllByPublished(pageable, published);}

    public Mono<Post> savePost(Post post){return this.postRepository.save(post);}

    public Mono<Post> getPostByPostId(String postId){return this.postRepository.findById(postId);}

    public Flux<Post> searchByHashTag(Pageable pageable, String hashTag, boolean published){return postRepository.findByHashTagContainingAndPublished(pageable, hashTag, published);}

    public Flux<Post> searchByContents(Pageable pageable, String contents, boolean published){return postRepository.findByContentsContainingAndPublished(pageable,contents, published);}

    public Mono<Comment> getCommentById(String id){return this.commentRepository.findById(id);}

    public Flux<Comment> getCommentsByPostId(String postId){return this.commentRepository.findByPostIdAndUpperCommentIdIsNullOrderByCreatedAtDesc(postId);}

    public Mono<Comment> saveComment(Comment comment){return this.commentRepository.save(comment);}

    public Mono<Long> getCommentCount(String postId, boolean published){return this.commentRepository.countByPostIdAndPublished(postId, published);}

    public Flux<Comment> getCommentsByUpperCommentId(String upperCommentId){return this.commentRepository.findByUpperCommentIdOrderByCreatedAtDesc(upperCommentId);}

    public Mono<Long> countByUsername(String username, boolean published){return this.postRepository.countByUsernameAndPublished(username, published);}

    public Mono<Long> countCommentByUsername(String username, boolean published){return this.commentRepository.countByUsernameAndPublished(username, published);}
}
