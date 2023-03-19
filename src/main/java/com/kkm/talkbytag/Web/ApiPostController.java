package com.kkm.talkbytag.Web;

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
import java.util.UUID;

@RestController
public class ApiPostController {

    private final PostService postService;

    @Value("${upload.path}")
    private String uploadPath;

    public ApiPostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/api/posts")
    public Flux<Post> getPosts(@RequestParam int offset, @RequestParam int limit){
        return postService.getPosts()
                .filter(Post::isPublished)
                .skip(offset)
                .take(limit);
    }

    @PostMapping("/api/posts")
    public Mono<Post> createPost(@RequestBody Post post){
        return postService.savePost(post);
    }

    @PutMapping("/api/posts/{postId}")
    public Mono<Post> updatePost(@PathVariable String postId, @RequestBody Post post){
        return postService.getPostByPostId(postId)
                .flatMap(p -> {
                    p.setContents(post.getContents());
                    p.setAuthorId(post.getAuthorId());
                    p.setHashTag(post.getHashTag());
                    p.setPublished(post.isPublished());
                    p.setModifiedAt(LocalDateTime.now());
                    return postService.savePost(p);
                });
    }

    @PostMapping("/api/posts/{postId}/comments")
    public Mono<Comment> createComment(@PathVariable String postId, @RequestBody Comment comment) {
        return postService.createComment(postId, comment);
    }

    // 이미지 업로드
    @RequestMapping(value = "/api/posts/upload-image", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<ImageUploadResponse>> uploadImage(@RequestPart("file") Mono<FilePart> filePartMono) {
        return filePartMono
                .flatMap(filePart -> {
                    String fileName = UUID.randomUUID().toString() + "_" + filePart.filename();
                    Path path = Paths.get(uploadPath, fileName);
                    try {
                        Files.createDirectories(path.getParent());
                    } catch (IOException e) {
                        return Mono.error(e);
                    }
                    return filePart.transferTo(new File(path.toString()))
                            .thenReturn(ResponseEntity.ok().body(new ImageUploadResponse("/images/" + fileName)));
                })
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
}
