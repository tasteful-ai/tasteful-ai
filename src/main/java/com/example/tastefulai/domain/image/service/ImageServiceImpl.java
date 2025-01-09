package com.example.tastefulai.domain.image.service;

import com.example.tastefulai.domain.image.dto.ImageResponseDto;
import com.example.tastefulai.domain.image.dto.ProfileResponseDto;
import com.example.tastefulai.domain.image.entity.Image;
import com.example.tastefulai.domain.image.repository.ImageRepository;
import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.member.repository.MemberRepository;
import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final MemberRepository memberRepository;
    private final ImageRepository imageRepository;
    private final S3Uploader s3Uploader;

    @Override
    public ImageResponseDto uploadImage(Long memberId, MultipartFile image) throws IOException {

        Member member = memberRepository.findById(memberId).orElseThrow(()->new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        if (member.getImage() != null) {
            imageRepository.delete(member.getImage());
        }

        Image uploadedImage = s3Uploader.uploadImage(member, image);

        imageRepository.save(uploadedImage);

        return new ImageResponseDto(uploadedImage.getImageUrl());
    }
}
