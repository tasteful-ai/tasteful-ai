package com.example.tastefulai.global.config.filter;

import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.member.repository.MemberRepository;
import com.example.tastefulai.global.common.service.RedisService;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisService redisService;

    // 요청시 실행하는 메서드
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 요청에서 JWT(토큰) 확인
        String token = resolveToken(httpServletRequest);

        if (token != null) {
            // 토큰 유효성 검증
            if (jwtProvider.validateToken(token)) {
                // 블랙리스트 확인
                if (redisService.isBlacklisted(token)) {
                    throw new CustomException(ErrorCode.INVALID_TOKEN);
                }

                // 토큰에서 이메일 추출
                String email = jwtProvider.getEmailFromToken(token);

                // 사용자 정보 확인
                Member member = memberRepository.findByEmail(email)
                        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

                // 인증 정보 설정
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(new MemberDetailsImpl(member), null, member.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                throw new CustomException(ErrorCode.INVALID_TOKEN);
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String resolveToken(HttpServletRequest httpServletRequest) {
        // 요청 Header를 통한 "Authorization" 값 추출
        String bearerToken = httpServletRequest.getHeader("Authorization");
        return (bearerToken != null && bearerToken.startsWith("Bearer ")) ? bearerToken.substring(7) : null;
    }
}