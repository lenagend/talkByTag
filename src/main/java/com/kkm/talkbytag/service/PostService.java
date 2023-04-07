package com.kkm.talkbytag.service;

import com.kkm.talkbytag.domain.Comment;
import com.kkm.talkbytag.domain.Post;
import com.kkm.talkbytag.domain.UserInfo;
import com.kkm.talkbytag.dto.CommentWithUserInfo;
import com.kkm.talkbytag.dto.PostWithUserInfo;
import com.kkm.talkbytag.repository.CommentRepository;
import com.kkm.talkbytag.repository.PostRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PostService {

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;

    private final UserInfoService userInfoService;

    @Value("${avatar.placeholder.path}")
    private String avatarPlaceholderPath;

    public PostService(PostRepository postRepository, CommentRepository commentRepository, UserInfoService userInfoService) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userInfoService = userInfoService;
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

    public Mono<PostWithUserInfo> getPostWithUserInfo(Post post) {
        return userInfoService.findByUsername(post.getUsername())
                .switchIfEmpty(Mono.defer(() -> {
                    UserInfo anonymousUserInfo = new UserInfo();
                    anonymousUserInfo.setNickname("익명");
                    anonymousUserInfo.setProfileImage(avatarPlaceholderPath);
                    return Mono.just(anonymousUserInfo);
                }))
                .flatMap(user -> {
                    PostWithUserInfo postWithUserInfo = new PostWithUserInfo();
                    postWithUserInfo.setId(post.getId());
                    postWithUserInfo.setHashTag(post.getHashTag());
                    postWithUserInfo.setUsername(post.getUsername());
                    postWithUserInfo.setContents(post.getContents());
                    postWithUserInfo.setCreatedAt(post.getCreatedAt());
                    postWithUserInfo.setModifiedAt(post.getModifiedAt());
                    postWithUserInfo.setLiked(0);
                    postWithUserInfo.setViewCount(0);
                    postWithUserInfo.setPublished(post.isPublished());
                    postWithUserInfo.setCommentCount(0L);

                    postWithUserInfo.setNickname(user.getNickname());
                    postWithUserInfo.setProfileImage(user.getProfileImage());

                    return this.getCommentCount(post.getId(), post.isPublished())
                            .map(commentCount -> {
                                postWithUserInfo.setCommentCount(commentCount);
                                return postWithUserInfo;
                            });
                });
    }

    public Mono<CommentWithUserInfo> getCommentWithUserInfo(Comment comment) {
        return userInfoService.findByUsername(comment.getUsername())
                .switchIfEmpty(Mono.defer(() -> {
                    UserInfo anonymousUserInfo = new UserInfo();
                    anonymousUserInfo.setNickname("익명");
                    anonymousUserInfo.setProfileImage(avatarPlaceholderPath);
                    return Mono.just(anonymousUserInfo);
                }))
                .map(user -> {
                    CommentWithUserInfo commentWithUserInfo = new CommentWithUserInfo();

                    commentWithUserInfo.setId(comment.getId());
                    commentWithUserInfo.setPostId(comment.getPostId());
                    commentWithUserInfo.setUpperCommentId(comment.getUpperCommentId());
                    commentWithUserInfo.setUsername(comment.getUsername());
                    commentWithUserInfo.setContents(comment.getContents());
                    commentWithUserInfo.setCreatedAt(comment.getCreatedAt());
                    commentWithUserInfo.setModifiedAt(comment.getModifiedAt());
                    commentWithUserInfo.setLiked(comment.getLiked());
                    commentWithUserInfo.setPublished(comment.isPublished());

                    commentWithUserInfo.setNickname(user.getNickname());
                    commentWithUserInfo.setProfileImage(user.getProfileImage());

                    return commentWithUserInfo;
                });
    }
}

