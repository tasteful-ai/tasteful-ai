package com.example.tastefulai.domain.image.service;

import com.example.tastefulai.domain.image.entity.Image;
import com.example.tastefulai.domain.member.entity.Member;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface S3Uploader {

    /**
     * 파일 확장자 검사 후 S3에 이미지 업로드
     */
    Image uploadImage(MultipartFile image);

    /**
     * 파일의 확장자를 검증 (png, jpeg, jpg)
     */
    void isValidExtension(MultipartFile image) throws IOException;

    /**
     * S3에서 이미지 영구삭제
     */
    void deleteS3Image(String imageName);
}
