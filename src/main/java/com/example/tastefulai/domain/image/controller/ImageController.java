package com.example.tastefulai.domain.image.controller;

import com.example.tastefulai.domain.image.dto.ImageResponseDto;
import com.example.tastefulai.domain.image.service.ImageService;
import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.global.common.dto.CommonResponseDto;
import com.example.tastefulai.global.config.auth.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/members/profiles/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    // 프로필 사진 업로드
    @PutMapping
    public ResponseEntity<CommonResponseDto<ImageResponseDto>> updateImage(@AuthenticationPrincipal MemberDetailsImpl memberDetailsImpl,
                                                                           @RequestParam(required = false) MultipartFile image) throws IOException {

        Long memberId = memberDetailsImpl.getId();

        ImageResponseDto imageResponseDto = imageService.uploadImage(memberId, image);

        return new ResponseEntity<>(new CommonResponseDto<>("프로필 이미지 업데이트 성공", imageResponseDto), HttpStatus.OK);
    }

    // 프로필 사진 삭제
    @DeleteMapping
    public ResponseEntity<CommonResponseDto<String>> deleteImage(@AuthenticationPrincipal MemberDetailsImpl memberDetailsImpl) {

        Long memberId = memberDetailsImpl.getId();

        imageService.deleteImage(memberId);

        return new ResponseEntity<>(new CommonResponseDto<>("프로필 이미지 삭제 성공", null), HttpStatus.OK);
    }
}