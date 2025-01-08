package com.example.tastefulai.domain.taste.service;

import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.member.service.MemberService;
import com.example.tastefulai.domain.taste.dto.TasteRequestDto;
import com.example.tastefulai.domain.taste.dto.TasteResponseDto;
import com.example.tastefulai.domain.taste.entity.Taste;
import com.example.tastefulai.domain.taste.repository.TasteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TasteServiceImpl implements TasteService {

    private final TasteRepository tasteRepository;
    private final MemberService memberService;

    @Override
    @Transactional
    public TasteResponseDto updateTaste(Long memberId, TasteRequestDto tasteRequestDto) {

        Member member = memberService.findById(memberId);
        Taste taste = tasteRepository.findByMember(member);

        taste.updateGenre(tasteRequestDto.getGenre());
        taste.updateLikeFood(tasteRequestDto.getLikeFood());
        taste.updateDisLikeFood(tasteRequestDto.getDislikeFood());
        taste.updateDietaryPreference(tasteRequestDto.getDietaryPreference());
        taste.updateSpicyLevel(tasteRequestDto.getSpicyLevel());

        tasteRepository.save(taste);
        return new TasteResponseDto(taste.getGenre(), taste.getLikeFood(), taste.getDislikeFood(),
                                    taste.getDietaryPreference(), taste.getSpicyLevel());
    }
}

