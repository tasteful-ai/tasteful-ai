package com.example.tastefulai.global.config.filter;

import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.member.repository.MemberRepository;
import com.example.tastefulai.domain.member.service.BlacklistService;
import com.example.tastefulai.global.config.auth.MemberDetailsImpl;
import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.CustomException;
import com.example.tastefulai.global.util.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    /**
     * JWT 토큰 유효성 검사
     *  블랙리스트 확인
     *  인증 정보 설정
     *  - 권한 검사는 Spring Security 설정(authorizeHttpRequests)에 위임
     */

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    private final BlacklistService blacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // 요청에서 JWT(토큰) 확인
            String token = extractToken(httpServletRequest);

            if (token != null) {
                validateTokenAndAuthenticate(httpServletRequest, token);
            }

            filterChain.doFilter(httpServletRequest, httpServletResponse);

        } catch (CustomException exception) {
            handleAuthenticationFailure(httpServletResponse, exception);
        }
    }

    
    /**
     * **** 공통 메서드 ***
     */

    // JWT 토큰을 요청 헤더에서 추출
    private String extractToken(HttpServletRequest httpServletRequest) {
        String bearerToken = httpServletRequest.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }


    // 토큰 유효성 및 블랙리스트 확인, 사용자 인증 처리
    private void validateTokenAndAuthenticate(HttpServletRequest httpServletRequest, String token) {
        // 토큰 유효성 검증
        if (!jwtProvider.validateToken(token)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        // 블랙리스트 확인
        if (blacklistService.isBlacklisted(token)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        // 토큰에서 이메일 추출
        String email = jwtProvider.getEmailFromToken(token);
        // 이메일로 사용자 조회
        Member member = findMemberByEmail(email);
        // 인증 정보 설정
        setAuthentication(member);
    }

    // 이메일로 멤버 조회
    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    // 인증 정보 설정
    private void setAuthentication(Member member) {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(new MemberDetailsImpl(member), null, member.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    // 인증 실패 처리
    private void handleAuthenticationFailure(HttpServletResponse httpServletResponse, CustomException exception) throws IOException {
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.setContentType("application/json");
        httpServletResponse.getWriter().write("{\"error\": \"" + exception.getErrorCode().getMessage() + "\"}");
    }
}