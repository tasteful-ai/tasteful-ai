package com.example.tastefulai.global.config;

import com.example.tastefulai.global.config.filter.JwtAuthFilter;
import com.example.tastefulai.global.constant.EndpointConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity  //@Configuration 어노테이션 포함 및 보안 기능 활성화
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    // 비밀번호 암호화(BCryptPasswordEncoder) 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // 애플리케이션 보안 필터 체인
    // 스프링시큐리티 버전에 맞게 코드 작성 -> 람다식
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        DefaultSecurityFilterChain build = httpSecurity
                //브라우저 팝업창 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)

                // CORS 설정 활성화
                .cors(cors -> cors.configure(httpSecurity))

                // CSRF 비활성화
                .csrf(AbstractHttpConfigurer::disable)

                // 요청 권한 설정
                .authorizeHttpRequests(
                        request -> request.requestMatchers(EndpointConstants.AUTH_SIGNUP, EndpointConstants.AUTH_LOGIN).permitAll()// 회원가입과 로그인 요청 허용
                                .requestMatchers("/ws-chat/**").permitAll()
                                .requestMatchers("/test").permitAll()

                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // OPTIONS 허용
                                .anyRequest().authenticated() // 그 외 요청 인증 필요
                )
                // JWT 인증 필터 추가
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
        return build;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

