package com.example.tastefulai.member;

import static org.mockito.Mockito.*;

import com.example.tastefulai.domain.member.service.BlacklistService;
import com.example.tastefulai.domain.member.service.MemberServiceImpl;
import com.example.tastefulai.global.util.JwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private BlacklistService blacklistService;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private MemberServiceImpl memberServiceImpl;

    private String token = "sample.jwt.token";

//    @DisplayName("blacklistService : addToBlacklist 메서드가 호출되는지 검증 확인")
//    @Test
//    void testLogout() {
//        // given
//        long expiryMillis = 3600000L; // 예시로 1시간 만료 시간
//        when(jwtProvider.getAccessTokenExpiryMillis()).thenReturn(expiryMillis);
//
//        // when
//        memberServiceImpl.logout(token);
//
//        //then
//        verify(blacklistService, times(1)).addToBlacklist(token, expiryMillis);
//    }
}
