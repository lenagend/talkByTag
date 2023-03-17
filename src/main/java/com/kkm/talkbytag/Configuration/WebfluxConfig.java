package com.kkm.talkbytag.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class WebfluxConfig implements WebFluxConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해 CORS 설정 적용
                .allowedOrigins("http://localhost:3000", "http://localhost:8080") // 모든 도메인에서의 요청을 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메서드 설정
                .allowedHeaders("*") // 모든 헤더 허용
                .allowCredentials(true) // 쿠키 및 자격 증명 정보 허용
                .maxAge(3600); // 사전 요청(pre-flight request) 캐싱 시간 설정
    }


}
