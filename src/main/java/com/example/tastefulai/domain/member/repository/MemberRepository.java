package com.example.tastefulai.domain.member.repository;

import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    default Member findByIdOrThrow(Long memberId) {
        return findById(memberId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }
}
