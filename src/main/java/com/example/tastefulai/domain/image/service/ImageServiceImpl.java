package com.example.tastefulai.domain.image.service;

import com.example.tastefulai.domain.image.dto.ImageResponseDto;
import com.example.tastefulai.domain.image.entity.Image;
import com.example.tastefulai.domain.image.repository.ImageRepository;
import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.member.repository.MemberRepository;
import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.BadRequestException;
import com.example.tastefulai.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final MemberRepository memberRepository;
    private final ImageRepository imageRepository;
    private final S3Uploader s3Uploader;

    @Override
    @Transactional
    public ImageResponseDto uploadImage(Member member, MultipartFile image) throws IOException {

        // 기존에 저장된 사진을 db와 S3에서 삭제
        if (member.getImage() != null) {
            deleteImage(member);
        }

        // S3에 사진 저장
        Image uploadedImage = s3Uploader.uploadImage(member, image);

        // DB 에 저장
        imageRepository.save(uploadedImage);

        return new ImageResponseDto(uploadedImage.getImageUrl());
    }

    @Override
    @Transactional
    public void deleteImage(Member member) {

        Image image = member.getImage();
        if (image == null) {
            throw new BadRequestException(ErrorCode.NO_IMAGE_TO_DELETE);
        }
        s3Uploader.deleteS3Image(image.getFileName());

        member.updateImage(null);
        imageRepository.delete(image);
        imageRepository.flush();
    }
}
