package com.core.controller;

import com.core.constant.ApiConstant;
import com.core.dto.response.ResponseDto;
import com.core.service.S3Service;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = ApiConstant.STORAGE)
@AllArgsConstructor
public class StorageController {

    private final S3Service s3Service;

    @GetMapping("/upload-url-s3")
    public ResponseEntity<ResponseDto> generateUploadUrlS3(@RequestParam String fileName) {
        String uploadUrl = s3Service.generateUploadUrl(fileName);
        return ResponseEntity.ok(ResponseDto.builder().status(true).message("Upload URL generated successfully").data(uploadUrl).build());
    }
}
