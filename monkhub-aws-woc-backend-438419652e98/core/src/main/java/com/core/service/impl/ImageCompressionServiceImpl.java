package com.core.service.impl;

import com.core.constant.AppConstant;
import com.core.dto.storage.GetCompressImageUrlRequestDto;
import com.core.property.S3Property;
import com.core.service.ImageCompressionService;
import com.core.utility.MethodLoggerUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageCompressionServiceImpl implements ImageCompressionService {

    private final S3Client s3Client;
    private final S3Property s3Property;

    @Override
    public String getCompressImageUrl(GetCompressImageUrlRequestDto dto) throws IOException, URISyntaxException {
        MethodLoggerUtility.start(this);

        // 1. Extract S3 key from URL
        URI uri = URI.create(dto.getImageUrl());
        String path = uri.getPath();
        String bucketName = s3Property.getBucketName();

        String key = path.startsWith("/") ? path.substring(1) : path;
        if (key.startsWith(bucketName + "/")) {
            key = key.substring(bucketName.length() + 1);
        }

        String extension = key.substring(key.lastIndexOf('.') + 1).toLowerCase();
        if (!AppConstant.COMPRESSIBLE_IMAGE_TYPES.contains(extension)) {
            log.warn("Unsupported image type for compression: {}", extension);
            return dto.getImageUrl();
        }

        // 2. Create temporary files
        File originalFile = File.createTempFile("original", "." + extension);
        File compressedFile = File.createTempFile("compressed", ".jpg");

        try {
            // 3. Download the S3 object to local temporary file
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            try (ResponseInputStream<GetObjectResponse> s3is = s3Client.getObject(getObjectRequest)) {
                Files.copy(s3is, originalFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            // 4. Compress the image using Thumbnailator
            Thumbnails.of(originalFile)
                    .scale(1.0)
                    .outputQuality(0.5)
                    .outputFormat("jpg")
                    .toFile(compressedFile);

            // 5. Build target key for compressed image
            String compressedKey = key.replaceFirst("(\\.[^.]+)?$", AppConstant.COMPRESS_IMAGE_SUFFIX);

            // 6. Upload compressed image back to S3
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(compressedKey)
                    .contentType("image/jpeg")
                    .build();
            s3Client.putObject(putObjectRequest, compressedFile.toPath());

            // 7. Construct permanent public URL of the compressed image
            String compressedUrl = String.format("https://%s.s3.%s.amazonaws.com/%s",
                    bucketName,
                    s3Property.getRegion(),
                    compressedKey);

            return compressedUrl;
        } finally {
            // 8. Clean up local temporary files
            try {
                Files.deleteIfExists(originalFile.toPath());
            } catch (IOException e) {
                log.error("Failed to delete temporary original file: {}", originalFile.getAbsolutePath(), e);
            }
            try {
                Files.deleteIfExists(compressedFile.toPath());
            } catch (IOException e) {
                log.error("Failed to delete temporary compressed file: {}", compressedFile.getAbsolutePath(), e);
            }
            MethodLoggerUtility.end(this);
        }
    }
}
