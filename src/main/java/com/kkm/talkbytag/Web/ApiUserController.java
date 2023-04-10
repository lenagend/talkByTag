package com.kkm.talkbytag.Web;

import com.kkm.talkbytag.domain.User;
import com.kkm.talkbytag.domain.UserInfo;
import com.kkm.talkbytag.dto.UserRegistrationDto;
import com.kkm.talkbytag.service.AuthenticationService;
import com.kkm.talkbytag.service.CustomReactiveUserDetailsService;
import com.kkm.talkbytag.service.PostService;
import com.kkm.talkbytag.service.UserInfoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ApiUserController {

    private final CustomReactiveUserDetailsService customReactiveUserDetailsService;

    private final AuthenticationService authenticationService;

    private final Validator userRegistrationDtoValidator;

    private final UserInfoService userInfoService;

    private final PostService postService;

    @Value("${avatar.placeholder.path}")
    private String avatarPlaceholderPath;

    public ApiUserController(CustomReactiveUserDetailsService customReactiveUserDetailsService, AuthenticationService authenticationService, Validator userRegistrationDtoValidator, UserInfoService userInfoService, PostService postService) {
        this.customReactiveUserDetailsService = customReactiveUserDetailsService;
        this.authenticationService = authenticationService;
        this.userRegistrationDtoValidator = userRegistrationDtoValidator;
        this.userInfoService = userInfoService;
        this.postService = postService;
    }


    @GetMapping("/userInfo")
    public Mono<ResponseEntity<UserInfo>> getUserInfo(@RequestHeader("Authorization") String authHeader) {
        try{
            String token = authHeader.replace("Bearer ", "");
            String username = authenticationService.extractUsername(token);

            Mono<UserInfo> userInfo = userInfoService.findByUsername(username);
            Mono<Long> postCount = postService.countByUsername(username, true)
                    .doOnSuccess(count -> System.out.println("postCount: " + count));
            Mono<Long> commentCount = postService.countCommentByUsername(username, true)
                    .doOnSuccess(count -> System.out.println("commentCount: " + count));

            return Mono.zip(userInfo, postCount, commentCount)
                    .map(tuple -> {
                        UserInfo ui = tuple.getT1();
                        Long pCount = tuple.getT2();
                        Long cCount = tuple.getT3();
                        ui.setPostCount(pCount);
                        ui.setCommentCount(cCount);
                        return ResponseEntity.ok(ui);
                    })
                    .defaultIfEmpty(ResponseEntity.notFound().build());
        }catch (Exception e){
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }

    }

    @PutMapping("/userInfo")
    public Mono<ResponseEntity<UserInfo>> updateUserInfo(@RequestHeader("Authorization") String authHeader, @RequestBody UserInfo updatedUserInfo) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String username = authenticationService.extractUsername(token);

            return userInfoService.updateUserInfo(username, updatedUserInfo)
                    .map(ResponseEntity::ok)
                    .defaultIfEmpty(ResponseEntity.notFound().build())
                    .onErrorResume(IllegalArgumentException.class, e -> Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(null)));
        } catch (Exception e) {
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }
    }



    @PostMapping("/register")
    public Mono<ResponseEntity<String>> register(@RequestBody Mono<UserRegistrationDto> registrationDtoMono) {

        return registrationDtoMono.flatMap(registrationDto -> {
            Errors validationErrors = new BeanPropertyBindingResult(registrationDto, "UserRegistrationDto");
            userRegistrationDtoValidator.validate(registrationDto, validationErrors);

            if (validationErrors.hasErrors()) {
                String errorMessage = validationErrors.getFieldErrors().stream()
                        .map(error -> error.getDefaultMessage())
                        .collect(Collectors.joining("\n"));
                return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage));
            }

            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
            User newUser = new User(registrationDto.getUsername(), registrationDto.getPassword(), authorities);

            return customReactiveUserDetailsService.findByUsername(newUser.getUsername())
                    .flatMap(user -> Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body("이미 등록된 사용자입니다.")))
                    .switchIfEmpty(customReactiveUserDetailsService.createUser(newUser)
                            .doOnNext(createdCustomUserDetails -> {
                                UserInfo tempUserInfo = new UserInfo();
                                tempUserInfo.setUsername(createdCustomUserDetails.getUsername());
                                tempUserInfo.setNickname("임시유저" + UUID.randomUUID().toString() + "님");
                                tempUserInfo.setProfileImage(avatarPlaceholderPath);
                                userInfoService.saveUserInfo(tempUserInfo).subscribe();
                            })
                            .thenReturn(ResponseEntity.ok().body("가입이 완료되었습니다.")));

        });
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<?>> login(@RequestBody User loginUser) {
        return customReactiveUserDetailsService.findByUsername(loginUser.getUsername())
                .flatMap(user -> {
                    if (user.getPassword().equals(loginUser.getPassword())) {
                        String token = authenticationService.generateToken(user.getUsername());
                        return Mono.just(ResponseEntity.ok().body(Collections.singletonMap("token", token)));
                    } else {
                        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
                    }
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @GetMapping("/verify")
    public Mono<ResponseEntity<Void>> verifyToken(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");

        if (authenticationService.validateToken(token)) {
            return Mono.just(new ResponseEntity<>(HttpStatus.OK));
        } else {
            return Mono.just(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
        }
    }
}
