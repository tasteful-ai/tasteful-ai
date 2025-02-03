package com.example.tastefulai.domain.image.service;

import com.example.tastefulai.domain.image.dto.ImageResponseDto;
import com.example.tastefulai.domain.image.entity.Image;
import com.example.tastefulai.domain.image.repository.ImageRepository;
import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final S3Uploader s3Uploader;
    private final MemberService memberService;

    @Override
    public ImageResponseDto uploadImage(Long memberId, MultipartFile image) throws IOException {

        // S3 업로드  -> member 저장을 안한 Image 반환
        Image uploadImage = s3Uploader.uploadImage(image);

        // Member 정보 세팅
        Member member = memberService.findById(memberId);
        uploadImage.updateMember(member);

        // 기존에 저장된 사진을 db와 S3에서 삭제
        deleteImage(memberId);

        // db에 새로운 사진 저장
        Image savedImage = imageRepository.save(uploadImage);

        return new ImageResponseDto(savedImage.getImageUrl());
    }

    @Override
    public void deleteImage(Long memberId) {
        Image existImage = imageRepository.findByMemberId(memberId);
        if (existImage != null) {
            s3Uploader.deleteS3Image(existImage.getFileName());
            imageRepository.delete(existImage);
        }
    }
}
