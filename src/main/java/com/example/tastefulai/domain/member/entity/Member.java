package com.example.tastefulai.domain.member.entity;

import com.example.tastefulai.domain.image.entity.Image;
import com.example.tastefulai.domain.member.enums.GenderRole;
import com.example.tastefulai.domain.member.enums.MemberRole;
import com.example.tastefulai.domain.taste.entity.Taste;
import com.example.tastefulai.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @Column
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GenderRole genderRole;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole memberRole;

    // 연관 관계
    @OneToOne(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Image image;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Taste> tastes = new ArrayList<>();

    // 사업자 전용 필드
    @Column(unique = true)
    private String businessNumber;

    @Column
    private String storeName;

    @Column
    private String storeAddress;

    // 생성자
    public Member(String email, String password, String nickname,
                  Integer age, GenderRole genderRole, MemberRole memberRole) {
            this.email = email;
            this.password = password;
            this.nickname = nickname;
            this.age = age;
            this.genderRole = genderRole;
            this.memberRole = memberRole;
    }

    // 사업자 회원 생성자
    public Member(String email, String password, String nickname,
                  Integer age, GenderRole genderRole, MemberRole memberRole,
                  String businessNumber, String storeName, String storeAddress
                  ) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.age = age;
        this.genderRole = genderRole;
//        this.memberRole = MemberRole.OWNER;
        this.businessNumber = businessNumber;
        this.storeName = storeName;
        this.storeAddress = storeAddress;

    }

}
