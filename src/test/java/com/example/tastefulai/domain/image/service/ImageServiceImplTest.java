package com.example.tastefulai.domain.image.service;

import com.example.tastefulai.domain.image.dto.ImageResponseDto;
import com.example.tastefulai.domain.image.entity.Image;
import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.member.enums.GenderRole;
import com.example.tastefulai.domain.member.enums.MemberRole;
import com.example.tastefulai.domain.member.service.MemberService;
import groovy.util.logging.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {

    private static final Logger log = LoggerFactory.getLogger(ImageServiceImplTest.class);

    @InjectMocks
    private ImageService imageService;

    @Mock
    private MemberService memberService;

    @Mock
    private S3Uploader s3Uploader;

    @Test
    @DisplayName("이미지 업로드 성공")
    void uploadImage_success() {
        // given
        Long memberId = 1L;
        Member mockMember = new Member(MemberRole.USER, "testUser@example.com", "passwOrd123@", "User", 24, GenderRole.MALE, null);
        MultipartFile image = new MockMultipartFile("image", "image.png", "image/png", new byte[10]);
        Image uploadedImage = new Image("uploadedName", image.getContentType(), image.getSize(), "https://image.png");

        // stubbing
        when(s3Uploader.uploadImage(image)).thenReturn(uploadedImage);
        when(memberService.findById(memberId)).thenReturn(mockMember);


        // when
        ImageResponseDto imageResponseDto = imageService.uploadImage(memberId, image);

        // then
        assertNotNull(imageResponseDto);
        assertEquals(imageResponseDto.getImageUrl(), uploadedImage.getImageUrl());

    }

    @Test
    void deleteImage_success() {
    }
}