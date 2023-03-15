package com.kkm.talkbytag.service;

import com.kkm.talkbytag.domain.Comment;
import com.kkm.talkbytag.domain.Post;
import com.kkm.talkbytag.repository.CommentRepository;
import com.kkm.talkbytag.repository.PostRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;

    public PostService(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public Flux<Post> getPosts(){return this.postRepository.findAll();}

    public Flux<Post> getNewestPosts(Pageable pageable){return this.postRepository.findAllBy(pageable);}

    public Mono<Post> savePost(Post post){return this.postRepository.save(post);}

    public Mono<Void> deletePostById(String id){return this.postRepository.deleteById(id);}

    public Mono<Comment> saveComment(Comment comment){return this.commentRepository.save(comment);}

    public Flux<Comment> getCommentsByPostId(String postId){return  this.commentRepository.findAllByPostId(postId);}
}
