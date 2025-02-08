package com.example.tastefulai.domain.image.service;

import com.example.tastefulai.domain.image.dto.ImageResponseDto;
import com.example.tastefulai.domain.image.entity.Image;
import com.example.tastefulai.domain.image.repository.ImageRepository;
import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.member.enums.GenderRole;
import com.example.tastefulai.domain.member.enums.MemberRole;
import com.example.tastefulai.domain.member.service.MemberServiceImpl;
import groovy.util.logging.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {

    @InjectMocks
    private ImageServiceImpl imageServiceImpl;

    @Mock
    private MemberServiceImpl memberServiceImpl;

    @Mock
    private S3UploaderImpl s3UploaderImpl;

    @Mock
    private ImageRepository imageRepository;

    @Test
    @DisplayName("이미지 업로드 성공")
    void uploadImage_success() {

        ImageServiceImpl spyImageService = spy(imageServiceImpl);
        Long memberId = 1L;
        Member mockMember = new Member(MemberRole.USER, "testUser@example.com", "passwOrd123@", "User", 24, GenderRole.MALE, null);
        MultipartFile image = new MockMultipartFile("image", "image.png", "image/png", new byte[10]);
        Image uploadedImage = new Image("uploadedName", image.getContentType(), image.getSize(), "https://image.png");

        when(s3UploaderImpl.uploadImage(image)).thenReturn(uploadedImage);
        when(memberServiceImpl.findById(memberId)).thenReturn(mockMember);
        when(imageRepository.save(uploadedImage)).thenReturn(uploadedImage);
        doNothing().when(spyImageService).deleteImage(any());

        ImageResponseDto imageResponseDto = spyImageService.uploadImage(memberId, image);

        assertNotNull(imageResponseDto);
        assertEquals(imageResponseDto.getImageUrl(), uploadedImage.getImageUrl());
    }

    @Test
    @DisplayName("이미지 삭제 성공 - 기존 이미지 존재 시")
    void deleteImage_success_withImage() {

        Long memberId = 1L;
        Image image = new Image("image.jpg", "image/jpg", 10L, "https://imageUrl.jpg");

        when(imageRepository.findByMemberId(memberId)).thenReturn(image);
        doNothing().when(s3UploaderImpl).deleteS3Image(any());
        doNothing().when(imageRepository).delete(any());

        imageServiceImpl.deleteImage(memberId);

        verify(s3UploaderImpl).deleteS3Image(image.getFileName());
        verify(imageRepository).delete(image);
    }

    @Test
    @DisplayName("이미지 삭제 성공 - 기존 이미지 없을 시")
    void deleteImage_success_withOutImage() {

        Long memberId = 1L;

        when(imageRepository.findByMemberId(memberId)).thenReturn(null);

        imageServiceImpl.deleteImage(memberId);

        verify(s3UploaderImpl, never()).deleteS3Image(any());
        verify(imageRepository, never()).delete(any(Image.class));
    }
}