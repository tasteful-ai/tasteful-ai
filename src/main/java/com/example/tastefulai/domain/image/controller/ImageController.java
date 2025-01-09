package com.example.tastefulai.domain.image.controller;

import com.example.tastefulai.domain.image.dto.ImageResponseDto;
import com.example.tastefulai.domain.image.dto.ProfileResponseDto;
import com.example.tastefulai.domain.image.service.ImageService;
import com.example.tastefulai.global.common.dto.CommonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController("/api/auth/members/{memberId}/profiles")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    //프로필 사진 업로드
    //이후 @PathVariable 의 memberId를 로그인한 유저의 정보로 받아오게 바꿀 것
    //ProfileResponseDto 의 tastes 바꿀것
    @PatchMapping
    public ResponseEntity<CommonResponseDto<ImageResponseDto>> updateProfile (@PathVariable Long memberId,
                                                                                @RequestParam(required = false) MultipartFile image) throws IOException {

        ImageResponseDto imageResponseDto = imageService.uploadImage(memberId, image);

        return new ResponseEntity<>(new CommonResponseDto<>("프로필 업데이트 성공", imageResponseDto), HttpStatus.OK);
    }
}