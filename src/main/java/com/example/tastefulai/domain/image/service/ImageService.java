package com.example.tastefulai.domain.image.service;

import com.example.tastefulai.domain.image.dto.ImageResponseDto;
import com.example.tastefulai.domain.member.entity.Member;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    ImageResponseDto uploadImage(Long memberId, MultipartFile image) throws IOException;

    void deleteImage(Long memberId);
}
