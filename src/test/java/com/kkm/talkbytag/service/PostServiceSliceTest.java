package com.kkm.talkbytag.service;

import com.kkm.talkbytag.domain.Post;
import com.kkm.talkbytag.repository.CommentRepository;
import com.kkm.talkbytag.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class PostServiceSliceTest {

    PostService postService;

    @MockBean
    PostRepository postRepository;

    @MockBean
    CommentRepository commentRepository;




}
