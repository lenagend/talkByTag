package com.kkm.talkbytag.Web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkm.talkbytag.dto.ImageUploadResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ApiFileController {

    @Value("${upload.path}")
    private String uploadPath;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(value = "/upload-image", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<?>> uploadImage(@RequestPart("file") Mono<FilePart> filePartMono) {
        return filePartMono
                .flatMap(filePart -> {
                    String originalFilename = filePart.filename();
                    String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".")); // 파일 확장자 추출
                    String fileName = UUID.randomUUID() + fileExtension; // 확장자를 포함한 파일명 생성
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
