package com.example.tastefulai.domain.image.service;

import com.example.tastefulai.domain.image.dto.ImageResponseDto;
import org.springframework.web.multipart.MultipartFile;


public interface ImageService {

    ImageResponseDto uploadImage(Long memberId, MultipartFile image);

    void deleteImage(Long memberId);
}
