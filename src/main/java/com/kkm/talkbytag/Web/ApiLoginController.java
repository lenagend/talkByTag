package com.kkm.talkbytag.Web;

import com.kkm.talkbytag.domain.User;
import com.kkm.talkbytag.dto.UserRegistrationDto;
import com.kkm.talkbytag.service.AuthenticationService;
import com.kkm.talkbytag.service.CustomReactiveUserDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Collections;
import java.util.stream.Collectors;

@RestController
public class ApiLoginController {

    private final CustomReactiveUserDetailsService customReactiveUserDetailsService;

    private final AuthenticationService authenticationService;

    public ApiLoginController(CustomReactiveUserDetailsService customReactiveUserDetailsService, AuthenticationService authenticationService) {
        this.customReactiveUserDetailsService = customReactiveUserDetailsService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRegistrationDto registrationDto, BindingResult result) {
        if (result.hasErrors()) {
            String errorMessage = result.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.joining("\n"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }

        // 요청 본문에 포함된 데이터를 validation 처리한 후, 새로운 사용자를 등록하는 로직을 수행
        // ...

        return ResponseEntity.ok().body("User registered successfully");
    }

    @PostMapping("/api/login")
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

    @GetMapping("/api/verify")
    public Mono<ResponseEntity<Void>> verifyToken(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");

        if (authenticationService.validateToken(token)) {
            return Mono.just(new ResponseEntity<>(HttpStatus.OK));
        } else {
            return Mono.just(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
        }
    }
}
