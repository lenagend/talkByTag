package com.kkm.talkbytag.service;

import com.kkm.talkbytag.domain.Comment;
import com.kkm.talkbytag.domain.Liked;
import com.kkm.talkbytag.domain.Post;
import com.kkm.talkbytag.domain.UserInfo;
import com.kkm.talkbytag.dto.CommentWithUserInfo;
import com.kkm.talkbytag.dto.PostWithUserInfo;
import com.kkm.talkbytag.repository.CommentRepository;
import com.kkm.talkbytag.repository.LikedRepository;
import com.kkm.talkbytag.repository.PostRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;

@Service
public class PostService {

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;

    private final LikedRepository likedRepository;

    private final UserInfoService userInfoService;

    @Value("${avatar.placeholder.path}")
    private String avatarPlaceholderPath;

    public PostService(PostRepository postRepository, CommentRepository commentRepository, LikedRepository likedRepository, UserInfoService userInfoService) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.likedRepository = likedRepository;
        this.userInfoService = userInfoService;
    }

    public Flux<Post> getPosts(Pageable pageable, boolean published){return this.postRepository.findAllByPublished(pageable, published);}

    public Mono<Post> savePost(Post post){return this.postRepository.save(post);}

    public Mono<Post> getPostByPostId(String postId){return this.postRepository.findById(postId);}

    public Flux<Post> searchByTitle(Pageable pageable, String title, boolean published){return postRepository.findByTitleContainingAndPublished(pageable, title, published);}

    public Flux<Post> searchByContents(Pageable pageable, String contents, boolean published){return postRepository.findByContentsContainingAndPublished(pageable,contents, published);}

    public Mono<Comment> getCommentById(String id){return this.commentRepository.findById(id);}

    public Flux<Comment> getCommentsByPostId(String postId){return this.commentRepository.findByPostIdAndUpperCommentIdIsNullOrderByCreatedAtDesc(postId);}

    public Mono<Comment> saveComment(Comment comment){return this.commentRepository.save(comment);}

    public Mono<Long> getCommentCount(String postId, boolean published){return this.commentRepository.countByPostIdAndPublished(postId, published);}

    public Flux<Comment> getCommentsByUpperCommentId(String upperCommentId){return this.commentRepository.findByUpperCommentIdOrderByCreatedAtDesc(upperCommentId);}

    public Mono<Long> countByUsername(String username, boolean published){return this.postRepository.countByUsernameAndPublished(username, published);}

    public Flux<Post> getPublishedPostsByUser(Pageable pageable, String username, boolean published){return this.postRepository.findByUsernameAndPublished(pageable, username, published);}

    public Flux<Comment> getPublishedCommentsByUser(Pageable pageable, String username, boolean published){return this.commentRepository.findByUsernameAndPublished(pageable, username, published);}

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
                    PostWithUserInfo postWithUserInfo = PostWithUserInfo.fromPost(post);
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

    public Mono<ResponseEntity<Void>> toggleLike(String username, String postId, String commentId) {
        if (postId != null && commentId != null) {
            return Mono.error(new IllegalArgumentException("postId와 commentId를 동시에 제공할 수 없습니다."));
        }
        if (postId == null && commentId == null) {
            return Mono.error(new IllegalArgumentException("postId 또는 commentId를 제공해야 합니다."));
        }

        Mono<Liked> existingLike;
        if (postId != null) {
            existingLike = likedRepository.findByUsernameAndPostId(username, postId);
        } else {
            existingLike = likedRepository.findByUsernameAndCommentId(username, commentId);
        }

        return existingLike
                .flatMap(liked -> {
                    // 이미 좋아요 상태인 경우, 좋아요 취소
                    return likedRepository.delete(liked)
                            .thenReturn(ResponseEntity.noContent().<Void>build());
                })
                .switchIfEmpty(Mono.defer(() -> {
                    // 좋아요 상태가 아닌 경우, 좋아요
                    Liked newLike = new Liked();
                    newLike.setUsername(username);
                    if (postId != null) {
                        newLike.setPostId(postId);
                    } else {
                        newLike.setCommentId(commentId);
                    }
                    return likedRepository.save(newLike)
                            .thenReturn(ResponseEntity.ok().<Void>build());
                }));
    }

    public Mono<Boolean> isPostLikedByUser(String postId, String username) {
        return likedRepository.existsByPostIdAndUsername(postId, username);
    }

    public Mono<Long> getPostLikeCount(String postId) {
        return likedRepository.countByPostId(postId);
    }

    public Mono<Boolean> isCommentLikedByUser(String commentId, String username) {
        return likedRepository.existsByCommentIdAndUsername(commentId, username);
    }

    public Mono<Long> getCommentLikeCount(String commentId) {
        return likedRepository.countByCommentId(commentId);
    }

    public Mono<Long> countPostsLikedByUsername(String username) {
        return likedRepository.countByUsername(username);
    }

    public Flux<Post> getLikedPostsByUsername(Pageable pageable, String username) {
        return likedRepository.findByUsername(username)
                .flatMap(liked -> postRepository.findByIdAndPublished(pageable, liked.getPostId(), true));
    }

    public Mono<Boolean> unpublishUserPosts(String username) {
        return postRepository.findByUsernameAndPublished(username, true)
                .flatMap(post -> {
                    post.setPublished(false);
                    return postRepository.save(post);
                })
                .collectList()
                .map(posts -> !posts.isEmpty());
    }

    public Mono<Boolean> unpublishUserComments(String username) {
        return commentRepository.findByUsernameAndPublished(username, true)
                .flatMap(comment -> {
                    comment.setPublished(false);
                    return commentRepository.save(comment);
                })
                .collectList()
                .map(comments -> !comments.isEmpty());
    }

    public Flux<PostWithUserInfo> getTopPostsByLikesOnDate(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        LocalDateTime startOfDay = startDate.atStartOfDay();
        LocalDateTime endOfDay = endDate.atTime(LocalTime.MAX);

        return likedRepository.findTopLikedPostsByDateRange(startOfDay, endOfDay, pageable)
                .flatMap(liked -> postRepository.findById(liked.getPostId())
                        .flatMap(post -> {
                            PostWithUserInfo postWithUserInfo = PostWithUserInfo.fromPost(post);

                            return getPostLikeCount(post.getId())
                                    .flatMap(likesCount -> {
                                        postWithUserInfo.setLiked(likesCount);

                                        return userInfoService.findByUsername(post.getUsername())
                                                .map(userInfo -> {
                                                    postWithUserInfo.setNickname(userInfo.getNickname());
                                                    postWithUserInfo.setProfileImage(userInfo.getProfileImage());
                                                    return postWithUserInfo;
                                                });
                                    });
                        }))
                .sort(Comparator.comparing(PostWithUserInfo::getLiked)
                        .thenComparing(PostWithUserInfo::getCreatedAt).reversed());
    }

}

