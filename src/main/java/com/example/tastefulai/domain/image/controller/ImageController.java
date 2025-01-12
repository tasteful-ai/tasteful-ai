package com.example.tastefulai.domain.image.controller;

import com.example.tastefulai.domain.image.dto.ImageResponseDto;
import com.example.tastefulai.domain.image.service.ImageService;
import com.example.tastefulai.global.common.dto.CommonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/members/{memberId}/profiles/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    // 프로필 사진 업로드
    //이후 @PathVariable 의 memberId를 로그인한 유저의 정보로 받아오게 바꿀 것
    //ProfileResponseDto 의 tastes 바꿀것
    @PutMapping
    public ResponseEntity<CommonResponseDto<ImageResponseDto>> updateImage(@PathVariable Long memberId,
                                                                           @RequestParam(required = false) MultipartFile image) throws IOException {

        ImageResponseDto imageResponseDto = imageService.uploadImage(memberId, image);

        return new ResponseEntity<>(new CommonResponseDto<>("프로필 이미지 업데이트 성공", imageResponseDto), HttpStatus.OK);
    }

    // 프로필 사진 삭제
    @DeleteMapping
    public ResponseEntity<CommonResponseDto<ImageResponseDto>> deleteImage(@PathVariable Long memberId) {

        imageService.deleteImage(memberId);

        ImageResponseDto imageResponseDto = new ImageResponseDto(
                "https://tasteful-ai-image-bucket.s3.ap-northeast-2.amazonaws.com/%E1%84%80%E1%85%B5%E1%84%87%E1%85%A9%E1%86%AB+%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5.png"
        );

        return new ResponseEntity<>(new CommonResponseDto<>("프로필 이미지 삭제 성공", imageResponseDto), HttpStatus.OK);
    }
}