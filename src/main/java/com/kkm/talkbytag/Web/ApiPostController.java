package com.kkm.talkbytag.Web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkm.talkbytag.domain.Comment;
import com.kkm.talkbytag.domain.Post;
import com.kkm.talkbytag.dto.ImageUploadResponse;
import com.kkm.talkbytag.service.PostService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ApiPostController {

    private final PostService postService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Value("${upload.path}")
    private String uploadPath;

    public ApiPostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    public ResponseEntity<Flux<Post>> getPosts(@RequestParam int offset, @RequestParam int limit){
        try {
            Flux<Post> result = postService.getPosts()
                    .filter(Post::isPublished)
                    .skip(offset)
                    .take(limit);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/posts/read/{id}")
    public ResponseEntity<Mono<Post>> getPostById(@PathVariable String id){
        try {
            Mono<Post> result = postService.getPostByPostId(id);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/posts/search")
    public ResponseEntity<Flux<Post>> searchByHashTag(@RequestParam("q") String q, @RequestParam int offset, @RequestParam int limit){
        boolean startsWithAtSign = q != null && !q.isEmpty() && q.charAt(0) == '@';
        Flux<Post> result;
        try {
            if (startsWithAtSign) {
                result = postService.searchByHashTag(q)
                        .filter(Post::isPublished)
                        .skip(offset)
                        .take(limit);
            } else {
                result = postService.searchByContents(q)
                        .filter(Post::isPublished)
                        .skip(offset)
                        .take(limit);
            }
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/posts")
    public ResponseEntity<Mono<Post>> createPost(@RequestBody Post post){
        try {
            Mono<Post> result = postService.savePost(post);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<Mono<Post>> updatePost(@PathVariable String postId, @RequestBody Post post){
        try {
            Mono<Post> result = postService.getPostByPostId(postId)
                    .flatMap(p -> {
                        Optional.ofNullable(post.getContents()).ifPresent(p::setContents);
                        Optional.ofNullable(post.getUsername()).ifPresent(p::setUsername);
                        Optional.ofNullable(post.getHashTag()).ifPresent(p::setHashTag);
                        Optional.ofNullable(post.isPublished()).ifPresent(p::setPublished);

                        p.setModifiedAt(LocalDateTime.now());
                        return postService.savePost(p);
                    });
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/comments")
    public ResponseEntity<Mono<Comment>> createComment(@RequestBody Comment comment) {
        try {
            comment.setAuthorId("testUser");
            Mono<Comment> result = postService.saveComment(comment);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<Flux<Comment>> getCommentsByPostId(@PathVariable String postId){
        try {
            Flux<Comment> result = this.postService.getCommentsByPostId(postId)
                    .filter(Comment::isPublished);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<Mono<Comment>> updateComment(@PathVariable String id, @RequestBody Comment comment) {
        try {
            Mono<Comment> result = postService.getCommentById(id)
                    .flatMap(c -> {
                        Optional.ofNullable(comment.getContents()).ifPresent(c::setContents);
                        Optional.ofNullable(comment.getAuthorId()).ifPresent(c::setAuthorId);
                        Optional.ofNullable(comment.isPublished()).ifPresent(c::setPublished);

                        c.setModifiedAt(LocalDateTime.now());
                        return postService.saveComment(c);
                    });
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/comments/count/{postId}")
    public ResponseEntity<Mono<Long>> getCommentCount(@PathVariable String postId) {
        try {
            Mono<Long> result = postService.getCommentCount(postId, true);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{upperCommentId}/replies")
    public ResponseEntity<Flux<Comment>> getCommentsByUpperCommentId(@PathVariable String upperCommentId) {
        try {
            Flux<Comment> result = this.postService.getCommentsByUpperCommentId(upperCommentId)
                    .filter(Comment::isPublished);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 이미지 업로드
    @RequestMapping(value = "/posts/upload-image", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<?>> uploadImage(@RequestPart("file") Mono<FilePart> filePartMono) {
        return filePartMono
                .flatMap(filePart -> {
                    String fileName = UUID.randomUUID().toString();
                    Path path = Paths.get(uploadPath, fileName);
                    try {
                        Files.createDirectories(path.getParent());
                    } catch (IOException e) {
                        return Mono.error(e);
                    }
                    return filePart.transferTo(new File(path.toString()))
                            .thenReturn(new ImageUploadResponse("/images/" + fileName));
                })
                .map(response -> {
                    try {
                        return ResponseEntity.ok().body(objectMapper.writeValueAsString(response));
                    } catch (JsonProcessingException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                })
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

}
