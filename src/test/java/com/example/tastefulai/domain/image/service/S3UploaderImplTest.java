package com.example.tastefulai.domain.image.service;

import com.example.tastefulai.domain.image.entity.Image;
import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.BadRequestException;
import org.apache.tika.Tika;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class S3UploaderImplTest {

    @InjectMocks
    private S3UploaderImpl s3Uploader;

    @Mock
    private S3Client s3Client;

    @Test
    @DisplayName("이미지 업로드 성공")
    void uploadImage_success() throws IOException {

        S3Uploader spyUploader = spy(s3Uploader);

        String originalName = "image.png";
        String bucket = "tasteful-ai-test";
        MultipartFile image = new MockMultipartFile("image", originalName, "image/png", new byte[10]);

        doNothing().when(spyUploader).isValidExtension(any());

        ReflectionTestUtils.setField(spyUploader, "bucket", "tasteful-ai-test");
        Image result = spyUploader.uploadImage(image);

        assertNotNull(result);
        assertNotNull(result.getFileName());
        assertTrue(result.getFileName().endsWith("_" + originalName.replaceAll("\\s", "_")));

        assertEquals(result.getFileSize(), image.getSize());
        assertEquals(result.getFileType(), image.getContentType());
        assertEquals(result.getImageUrl(), "https://" + bucket + ".s3.amazonaws.com/" + result.getFileName());

        verify(s3Client).putObject((PutObjectRequest) any(), (RequestBody) any());
    }

    @Test
    @DisplayName("이미지 확장자 검사 성공")
    void isValidExtension_success() throws IOException {

            String originalName = "typeTest.jpg";
            String contentType = "image/jpg";

            String filePath = "src/test/resources/typeTest.jpg";    // 이름 확장자는 jpg 이지만 MIME Type 은 png
            File file = new File(filePath);
            FileInputStream fileInputStream = new FileInputStream(file);

            MultipartFile image = new MockMultipartFile(originalName, originalName, contentType, fileInputStream);

            assertDoesNotThrow(() -> s3Uploader.isValidExtension(image));
    }

    @Test
    @DisplayName("이미지 확장자 검사 실패 - 잘못된 확장자명 예외처리")
    void isValidExtension_invalidExtension() {

        MockMultipartFile file = new MockMultipartFile("image", "image.exe", "image/jpg", new byte[1]);

        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> s3Uploader.isValidExtension(file));
        assertEquals(ErrorCode.INVALID_FILE, badRequestException.getErrorCode());
    }

    @Test
    @DisplayName("이미지 확장자 검사 실패 - 잘못된 MIME 타입 예외처리")
    void isValidExtension_invalidMIMEType() throws IOException {

            String originalName = "MIMETypeTest.jpeg";
            String contentType = "image/jpeg";
            String filePath = "src/test/resources/MIMETypeTest.jpeg";    // 이름 확장자는 jpg 이지만 MIME Type 은 png
            File file = new File(filePath);
            Tika tika = new Tika();

            FileInputStream fileInputStream = new FileInputStream(file);
            MultipartFile image = new MockMultipartFile(originalName, originalName, contentType, fileInputStream);

            BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> s3Uploader.isValidExtension(image));
            assertEquals("image/webp", tika.detect(image.getInputStream()));
            assertEquals(ErrorCode.INVALID_FILE, badRequestException.getErrorCode());
    }

    @Test
    @DisplayName("이미지 삭제 성공 - S3Client 호출 확인")
    void deleteS3Image_success() {
        DeleteObjectResponse deleteObjectResponse = DeleteObjectResponse.builder().build();
        when(s3Client.deleteObject(builder -> builder.bucket(any()))).thenReturn(deleteObjectResponse);

        assertDoesNotThrow(() -> s3Uploader.deleteS3Image("imageName"));

        verify(s3Client, times(1)).deleteObject(any(DeleteObjectRequest.class));
    }
}