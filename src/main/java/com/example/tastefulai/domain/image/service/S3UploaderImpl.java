package com.example.tastefulai.domain.image.service;

import com.example.tastefulai.domain.image.entity.Image;
import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.BadRequestException;
import com.example.tastefulai.global.error.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;

import org.apache.tika.Tika;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class S3UploaderImpl implements S3Uploader {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final Set<String> ALLOWED_EXTENSIONS = Set.of("png", "jpeg", "jpg");
    private final Set<String> ALLOWED_MIMETYPE = Set.of("image/png", "image/jpeg", "image/jpg");

    @Transactional
    @Override
    public Image uploadImage(MultipartFile image) {

        isValidExtension(image);

        String originalName = image.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String uniqueName = uuid + "_" + originalName.replaceAll("\\s", "_");

        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(uniqueName)
                            .contentType(image.getContentType())
                            .build(),
                    RequestBody.fromInputStream(image.getInputStream(), image.getSize())
            );
        } catch (IOException ioException) {
            throw new CustomException(ErrorCode.S3CLIENT_ERROR);
        }

        String imageUrl = String.format("https://%s.s3.amazonaws.com/%s", bucket, uniqueName);

        return new Image(uniqueName, image.getContentType(), image.getSize(), imageUrl);
    }

    @Override
    public void isValidExtension(MultipartFile image) {

        try {
            String extension = StringUtils.getFilenameExtension(image.getOriginalFilename());
            log.info("이름 확장자: {}", extension);

            if (!ALLOWED_EXTENSIONS.contains(extension)) {
                throw new BadRequestException(ErrorCode.INVALID_FILE);
            }

            Tika tika = new Tika();
            String mimeType = tika.detect(image.getInputStream());
            log.info("MIME type: {}", mimeType);

            if (!ALLOWED_MIMETYPE.contains(mimeType)) {
                throw new BadRequestException(ErrorCode.INVALID_FILE);
            }
        } catch (IOException ioException) {
            throw new CustomException(ErrorCode.FILE_ACCESS_DENIED);
        }
    }

    @Override
    @Transactional
    public void deleteS3Image(String imageName) {
            s3Client.deleteObject(builder -> builder.bucket(bucket).key(imageName).build());
    }
}
