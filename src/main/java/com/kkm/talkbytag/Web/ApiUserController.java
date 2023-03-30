package com.kkm.talkbytag.Web;

import com.kkm.talkbytag.domain.User;
import com.kkm.talkbytag.dto.UserRegistrationDto;
import com.kkm.talkbytag.service.AuthenticationService;
import com.kkm.talkbytag.service.CustomReactiveUserDetailsService;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ApiUserController {

    private final CustomReactiveUserDetailsService customReactiveUserDetailsService;

    private final AuthenticationService authenticationService;

    private final Validator userRegistrationDtoValidator;

    public ApiUserController(CustomReactiveUserDetailsService customReactiveUserDetailsService, AuthenticationService authenticationService, Validator userRegistrationDtoValidator) {
        this.customReactiveUserDetailsService = customReactiveUserDetailsService;
        this.authenticationService = authenticationService;
        this.userRegistrationDtoValidator = userRegistrationDtoValidator;
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
