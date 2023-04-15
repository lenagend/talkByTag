package com.kkm.talkbytag.Web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkm.talkbytag.dto.ImageUploadResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ApiFileController {

    @Value("${upload.path}")
    private String uploadPath;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping(value = "/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<?>> uploadImage(@RequestPart("file") Mono<FilePart> filePartMono,  @RequestPart("imageType") String imageType) {
        System.out.println("Received imageType: " + imageType); // 로깅 추가

        List<String> allowedExtensions = Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".bmp");

        return filePartMono
                .flatMap(filePart -> {
                    String originalFilename = filePart.filename();
                    String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase(); // 파일 확장자 추출 및 소문자 변환

                    if (!allowedExtensions.contains(fileExtension)) {
                        return Mono.error(new IllegalArgumentException("Invalid image file format."));
                    }

                    String fileName = UUID.randomUUID() + fileExtension; // 확장자를 포함한 파일명 생성
                    Path path = Paths.get(uploadPath, imageType, fileName); // 경로에 imageType을 추가

                    // 경로가 없으면 생성
                    try {
                        Files.createDirectories(path.getParent());
                    } catch (IOException e) {
                        return Mono.error(e);
                    }

                    return filePart.transferTo(new File(path.toString()))
                            .thenReturn(new ImageUploadResponse("/images/" + imageType + "/" + fileName));
                })
                .map(response -> {
                    try {
                        return ResponseEntity.ok().body(objectMapper.writeValueAsString(response));
                    } catch (JsonProcessingException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                })
                .onErrorResume(IllegalArgumentException.class, e -> Mono.just(ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()))))
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    public class ErrorResponse {
        private String errorMessage;

        public ErrorResponse(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }

}
