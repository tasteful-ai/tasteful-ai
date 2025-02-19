package com.example.tastefulai.member;

import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.member.enums.GenderRole;
import com.example.tastefulai.domain.member.enums.MemberRole;
import com.example.tastefulai.domain.member.repository.AdminMemberRepository;
import com.example.tastefulai.domain.member.service.AdminMemberServiceImpl;
import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.CustomException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminMemberServiceImplTest {

    @InjectMocks
    private AdminMemberServiceImpl adminMemberService;

    @Mock
    private AdminMemberRepository adminMemberRepository;

    @Mock
    private RedisTemplate<String, Object> blacklistTemplate;

    @Mock
    private Authentication authentication;

    private Member admin;
    private Member user;

    @BeforeEach
    void setUp() {

        LocalDateTime now = LocalDateTime.now();
        admin = new Member(MemberRole.ADMIN, "admin@example.com", "password", "AdminUser", 35, GenderRole.FEMALE, now);
        user = new Member(MemberRole.USER, "user@example.com", "password", "NormalUser", 25, GenderRole.MALE, now);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("회원 탈퇴 성공 - ADMIN이 일반 회원을 삭제")
    void deleteMemberByAdmin_Success() {

        Long memberId = 2L;
        when(authentication.getAuthorities()).thenReturn((Collection) List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        when(adminMemberRepository.findById(memberId)).thenReturn(Optional.of(user));

        adminMemberService.deleteMemberByAdmin(memberId);

        assertNotNull(user.getDeletedAt());
        verify(adminMemberRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("예외 - ADMIN 권한이 없으면 회원 탈퇴 불가")
    void deleteMemberByAdmin_NoAdminPermission() {
        Long memberId = 2L;
        when(authentication.getAuthorities()).thenReturn((Collection) List.of(new SimpleGrantedAuthority("ROLE_USER")));

        CustomException exception = assertThrows(CustomException.class,
                () -> adminMemberService.deleteMemberByAdmin(memberId));

        assertEquals(ErrorCode.ADMIN_PERMISSION_REQUIRED, exception.getErrorCode());
    }

    @Test
    @DisplayName("예외 - 존재하지 않는 회원 삭제 시")
    void deleteMemberByAdmin_MemberNotFound() {
        Long memberId = 99L;
        when(authentication.getAuthorities()).thenReturn((Collection) List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        when(adminMemberRepository.findById(memberId)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class,
                () -> adminMemberService.deleteMemberByAdmin(memberId));

        assertEquals(ErrorCode.MEMBER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("예외 - ADMIN이 다른 ADMIN을 삭제하려 할 때")
    void deleteMemberByAdmin_CannotDeleteAdmin() {
        Long adminId = 1L;
        when(authentication.getAuthorities()).thenReturn((Collection) List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        when(adminMemberRepository.findById(adminId)).thenReturn(Optional.of(admin));

        CustomException exception = assertThrows(CustomException.class,
                () -> adminMemberService.deleteMemberByAdmin(adminId));

        assertEquals(ErrorCode.ADMIN_CANNOT_REMOVE_ADMIN, exception.getErrorCode());
    }
}