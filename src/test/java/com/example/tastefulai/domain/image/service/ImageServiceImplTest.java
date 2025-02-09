package com.example.tastefulai.domain.image.service;

import com.example.tastefulai.domain.image.dto.ImageResponseDto;
import com.example.tastefulai.domain.image.entity.Image;
import com.example.tastefulai.domain.image.repository.ImageRepository;
import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.member.enums.GenderRole;
import com.example.tastefulai.domain.member.enums.MemberRole;
import com.example.tastefulai.domain.member.repository.MemberRepository;
import com.example.tastefulai.domain.member.service.MemberServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {

    @InjectMocks
    private ImageServiceImpl imageServiceImpl;

    @Mock
    private MemberServiceImpl memberServiceImpl;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private S3UploaderImpl s3UploaderImpl;

    private Member member;

    private MultipartFile imageFile;

    private Image image;

    @BeforeEach
    void setUp() {
        member = new Member(1L, new ArrayList<>());
        imageFile = new MockMultipartFile("image", "image.png", "image/png", new byte[10]);
        image = new Image("image", "image/png", 100L, "http://imageUrl.jpg", member);
    }

    @Test
    @DisplayName("이미지 업로드 성공")
    void uploadImage_success() {

        Image uploadedImage = image;
        ImageServiceImpl spyImageService = spy(imageServiceImpl);

        when(s3UploaderImpl.uploadImage(imageFile)).thenReturn(uploadedImage);
        when(memberServiceImpl.findById(1L)).thenReturn(member);
        when(imageRepository.save(uploadedImage)).thenReturn(uploadedImage);
        doNothing().when(spyImageService).deleteImage(any());

        ImageResponseDto imageResponseDto = spyImageService.uploadImage(1L, imageFile);

        assertNotNull(imageResponseDto);
        assertEquals(imageResponseDto.getImageUrl(), uploadedImage.getImageUrl());

        verify(s3UploaderImpl).uploadImage(imageFile);
    }

    @Test
    @DisplayName("이미지 삭제 성공 - 기존 이미지 존재 시")
    void deleteImage_success_withImage() {

        when(imageRepository.findByMemberId(1L)).thenReturn(image);
        doNothing().when(s3UploaderImpl).deleteS3Image(any());
        doNothing().when(imageRepository).delete(any());

        imageServiceImpl.deleteImage(1L);

        verify(s3UploaderImpl).deleteS3Image(image.getFileName());
        verify(imageRepository).delete(image);
    }

    @Test
    @DisplayName("이미지 삭제 성공 - 기존 이미지 없을 시")
    void deleteImage_success_withOutImage() {

        when(imageRepository.findByMemberId(1L)).thenReturn(null);

        imageServiceImpl.deleteImage(1L);

        verify(s3UploaderImpl, never()).deleteS3Image(any());
        verify(imageRepository, never()).delete(any(Image.class));
    }
}