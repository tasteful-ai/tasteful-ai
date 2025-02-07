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
    Optional<Member> findByEmail (String email);

    @Query("SELECT m FROM Member m WHERE m.email = :email AND m.deletedAt IS NULL")
    Optional<Member> findActiveByEmail(@Param("email") String email);

    @Query("SELECT m FROM Member m " +
            "LEFT JOIN FETCH m.tasteGenres tg LEFT JOIN FETCH tg.genres " +
            "LEFT JOIN m.tasteLikeFoods tl LEFT JOIN tl.likeFoods " +
            "LEFT JOIN m.tasteDislikeFoods td LEFT JOIN td.dislikeFoods " +
            "LEFT JOIN m.tasteDietaryPreferences tp LEFT JOIN tp.dietaryPreferences " +
            "LEFT JOIN m.tasteSpicyLevels ts LEFT JOIN ts.spicyLevel " +
            "WHERE m.id = :id")

    Optional<Member> findMemberWithTasteById(@Param("id") Long id);
}
