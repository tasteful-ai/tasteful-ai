package com.example.tastefulai.domain.member.entity;

import com.example.tastefulai.domain.image.dto.ProfileResponseDto;
import com.example.tastefulai.domain.image.entity.Image;
import com.example.tastefulai.domain.member.enums.GenderRole;
import com.example.tastefulai.domain.member.enums.MemberRole;
import com.example.tastefulai.domain.taste.entity.Taste;
import com.example.tastefulai.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Entity
@Table(name = "member")
@NoArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GenderRole genderRole;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole memberRole;

    private LocalDateTime deletedAt;

    // 연관 관계
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Image image;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Taste> tastes = new ArrayList<>();

    public List<GrantedAuthority> getAuthorities() {
        return List.of(memberRole)
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());
    }

    // 생성자
    public Member(String email, String password, String nickname, Integer age,
                  GenderRole genderRole, MemberRole memberRole, LocalDateTime deletedAt) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.age = age;
        this.genderRole = genderRole;
        this.memberRole = memberRole;
        this.deletedAt = deletedAt;
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateImage(Image image) {
        this.image = image;
    }

    public static ProfileResponseDto toProfileDto(Member member) {

        String memberImageUrl = (member.getImage() == null) ? null : member.getImage().getImageUrl();

        return new ProfileResponseDto(member.getNickname(), memberImageUrl, member.getCreatedAt().toLocalDate(), "");
    }
}
