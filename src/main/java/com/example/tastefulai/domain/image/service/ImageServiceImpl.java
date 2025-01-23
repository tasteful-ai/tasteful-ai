package com.example.tastefulai.domain.image.service;

import com.example.tastefulai.domain.image.dto.ImageResponseDto;
import com.example.tastefulai.domain.image.entity.Image;
import com.example.tastefulai.domain.image.repository.ImageRepository;
import com.example.tastefulai.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl {

    private final ImageRepository imageRepository;
    private final S3Uploader s3Uploader;

    public ImageResponseDto uploadImage(Member member, MultipartFile image) throws IOException {

        // S3 업로드
        String imgUrl = s3Uploader.uploadImage(image);

        // 기존에 저장된 사진을 db와 S3에서 삭제
        deleteImage(member);

        // db에 새로운 사진 저장
        Image savedImage = imageRepository.save(new Image(image.getOriginalFilename(), image.getContentType(), image.getSize(), imgUrl, member));

        return new ImageResponseDto(savedImage.getImageUrl());
    }

    public void deleteImage(Member member) {
        Image existImage = imageRepository.findByMemberId(member.getId());
        s3Uploader.deleteS3Image(existImage.getFileName());
        imageRepository.delete(existImage);
    }
}
