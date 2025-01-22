//package com.example.tastefulai.member;
//
//import com.example.tastefulai.domain.member.enums.GenderRole;
//import com.example.tastefulai.domain.member.enums.MemberRole;
//import com.example.tastefulai.domain.member.service.MemberService;
//import com.example.tastefulai.global.config.SecurityConfig;
//import com.example.tastefulai.global.error.errorcode.ErrorCode;
//import com.example.tastefulai.global.error.exception.CustomException;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.doThrow;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Import(SecurityConfig.class)
//class MemberPasswordVerificationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private MemberService memberService;
//
//    // 테스트 데이터 초기화
//    @BeforeAll
//    static void setUp(@Autowired MemberService memberService) {
//        String email = "usertest4@gmail.com";
//        String password = "Password123!";
//        String nickname = "TestAdmins";
//        Integer age = 30;
//        GenderRole genderRole = GenderRole.MALE; // 성별 역할
//        MemberRole memberRole = MemberRole.USER; // 기본 사용자 역할
//
//        // 테스트 데이터 생성
//        try {
//            memberService.signup(email, password, nickname, age, genderRole, memberRole);
//
//        } catch (CustomException exception) {
//            System.out.println("테스트 데이터 생성 중복 발생" + exception.getMessage());
//        }
//    }
//
//    @Test
//    @WithMockUser(username = "testUser", roles = "USER")
//    @DisplayName("성공: 비밀번호 검증이 성공적으로 완료될 경우")
//    void verifyPassword_shouldReturnOk_whenPasswordIsValid() throws Exception {
//        // Given
//        Long memberId = 1L;
//        String password = "Password123!";
//
//        doNothing().when(memberService).verifyPassword(memberId, password);
//
//        // When & Then
//        mockMvc.perform(post("/api/auth/members/{memberId}/check", memberId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"password\": \"" + password + "\"}"))
//                .andExpect(status().isOk());   //현재 결과값 : 400, Bad Request
//
//    }
//
//    @Test
//    @WithMockUser(roles = "USER")
//    @DisplayName("실패: 로그인된 사용자와 요청의 memberId가 불일치할 경우")
//    void verifyPassword_shouldReturnUnauthorized_whenUserIdDoesNotMatch() throws Exception {
//        // Given
//        Long memberId = 1L;
//        String password = "somePassword";
//
//        doThrow(new CustomException(ErrorCode.UNAUTHORIZED_MEMBER))
//                .when(memberService).verifyPassword(memberId, password);
//
//        // When & Then
//        mockMvc.perform(post("/api/auth/members/{memberId}/check", memberId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"password\": \"" + password + "\"}"))
//                .andExpect(status().isUnauthorized());
//    }
//}
