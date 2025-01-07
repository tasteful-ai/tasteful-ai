package com.example.tastefulai.global.config.filter;

import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.member.repository.MemberRepository;
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

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    // 요청시 실행하는 메서드
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 요청에서 JWT(토큰) 확인
        String token = resolveToken(httpServletRequest);

        // 토큰 및 유효 여부 확인
        if (token != null && jwtProvider.validateToken(token)) {
            String email = jwtProvider.getEmailFromToken(token);

            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

            // 인증 객체 생성 및 보안 컨텍스트 설정
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(member, null, member.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    // JWT(토큰) 추출 메서드
    private String resolveToken(HttpServletRequest httpServletRequest) {
        // 요청 Header를 통한 "Authorization" 값 추출
        String bearerToken = httpServletRequest.getHeader("Authorization");
        return (bearerToken != null && bearerToken.startsWith("Bearer ")) ? bearerToken.substring(7) : null;
    }
}