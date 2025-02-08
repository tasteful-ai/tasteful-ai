package com.example.tastefulai.domain.member.repository;

import com.example.tastefulai.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmailOrNickname(String email, String nickname);

    Optional<Member> findByEmail(String email);

    @Query("SELECT m FROM Member m WHERE m.email = :email AND m.deletedAt IS NULL")
    Optional<Member> findActiveByEmail(@Param("email") String email);


    @Query("SELECT DISTINCT m FROM Member m " +
            "LEFT JOIN FETCH m.tasteGenres tg LEFT JOIN FETCH tg.genres " +
            "WHERE m.id = :id")
    Optional<Member> findMemberWithTasteGenresById(@Param("id") Long id);

    @Query("SELECT DISTINCT m FROM Member m " +
            "LEFT JOIN FETCH m.tasteLikeFoods tl LEFT JOIN FETCH tl.likeFoods " +
            "WHERE m IN :member")
    Optional<Member> findMemberWithTasteLikeFoodsByMember(@Param("member") Member member);

    @Query("SELECT DISTINCT m FROM Member m " +
            "LEFT JOIN FETCH m.tasteDislikeFoods td LEFT JOIN FETCH td.dislikeFoods " +
            "WHERE m IN :member")
    Optional<Member> findMemberWithTasteDislikeFoodsByMember(@Param("member") Member member);

    @Query("SELECT DISTINCT m FROM Member m " +
            "LEFT JOIN FETCH m.tasteDietaryPreferences tp LEFT JOIN FETCH tp.dietaryPreferences " +
            "WHERE m IN :member")
    Optional<Member> findMemberWithTasteDietaryPreferencesByMember(@Param("member") Member member);

    @Query("SELECT DISTINCT m FROM Member m " +
            "LEFT JOIN FETCH m.tasteSpicyLevels ts LEFT JOIN FETCH ts.spicyLevel " +
            "WHERE m IN :member")
    Optional<Member> findMemberWithTasteSpicyLevelsByMember(@Param("member") Member member);
}

// 아래 코드는 성능 최적화 비교 테스트를 진행한 뒤 가장 빠른 최적화 코드로 수정하여 주석 삭제할 예정

//    @Query("SELECT DISTINCT m FROM Member m " +
//            "LEFT JOIN FETCH m.tasteGenres tg LEFT JOIN FETCH tg.genres " +
//            "WHERE m.id = :id")
//    Optional<Member> findMemberWithTasteGenresById(@Param("id") Long id);
//
//    @Query("SELECT DISTINCT m FROM Member m " +
//            "LEFT JOIN FETCH m.tasteLikeFoods tl LEFT JOIN FETCH tl.likeFoods " +
//            "LEFT JOIN m.tasteDislikeFoods td LEFT JOIN td.dislikeFoods " +
//            "LEFT JOIN m.tasteDietaryPreferences tp LEFT JOIN tp.dietaryPreferences " +
//            "LEFT JOIN m.tasteSpicyLevels ts LEFT JOIN ts.spicyLevel " +
//            "WHERE m IN :members")
//
//    Optional<Member> findMemberWithOtherTastesByMembers(@Param("members") List<Member> members);
//
//
//}

//    @Query("SELECT m FROM Member m " +
//            "LEFT JOIN FETCH m.tasteGenres tg LEFT JOIN FETCH tg.genres " +
//            "LEFT JOIN m.tasteLikeFoods tl LEFT JOIN tl.likeFoods " +
//            "LEFT JOIN m.tasteDislikeFoods td LEFT JOIN td.dislikeFoods " +
//            "LEFT JOIN m.tasteDietaryPreferences tp LEFT JOIN tp.dietaryPreferences " +
//            "LEFT JOIN m.tasteSpicyLevels ts LEFT JOIN ts.spicyLevel " +
//            "WHERE m.id = :id")
//
//    Optional<Member> findMemberWithTasteById(@Param("id") Long id);
//}
