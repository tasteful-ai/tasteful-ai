package com.example.tastefulai.global.config.auth;

import com.example.tastefulai.domain.member.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
@Slf4j(topic = "Security::UserDetailsImpl")
public class MemberDetailsImpl implements UserDetails {

    /**
     * Member entity.
     */
    private final Member member;

    /**
     * 계정의 권한 리스트를 리턴.
     *
     * @return {@code Collection<? extends GrantedAuthority>}
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return member.getMemberRole().getAuthorities().stream()
                .map(role -> new SimpleGrantedAuthority(role)) // String -> GrantedAuthority
                .collect(Collectors.toList());
    }

    /**
     * 사용자의 자격 증명 반환.
     *
     * @return 암호
     */
    @Override
    public String getPassword() {
        return this.member.getPassword();
    }

    /**
     * 사용자의 자격 증명 반환.
     *
     * @return 사용자 이름
     */
    @Override
    public String getUsername() {
        return this.member.getEmail();
    }

    /**
     * 계정 만료.
     *
     * @return 사용 여부
     * @apiNote 사용하지 않을 경우 true를 리턴하도록 재정의.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정 잠금.
     *
     * @return 사용 여부
     * @apiNote 사용하지 않을 경우 true를 리턴하도록 재정의.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 자격 증명 만료.
     *
     * @return 사용 여부
     * @apiNote 사용하지 않을 경우 true를 리턴하도록 재정의.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 계정 활성화.
     *
     * @return 사용 여부
     * @apiNote 사용할 경우 true를 리턴하도록 재정의.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
