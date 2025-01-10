package com.example.tastefulai.domain.taste.service;

import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.member.service.MemberService;
import com.example.tastefulai.domain.taste.dto.TasteResponseDto;
import com.example.tastefulai.domain.taste.entity.Taste;
import com.example.tastefulai.domain.taste.repository.TasteRepository;
import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.CustomException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TasteServiceImpl implements TasteService {

    private final TasteRepository tasteRepository;
    private final MemberService memberService;

    @Override
    @Transactional
    public TasteResponseDto updateGenre(Long memberId, String genre) {

        Member member = memberService.findById(memberId);
        Optional<Taste> optionalTaste = tasteRepository.findByMember(member);

        Taste taste = optionalTaste.orElse(new Taste(null, null, null, null, null, member));

        taste.updateGenre(genre);
        tasteRepository.save(taste);

        return new TasteResponseDto(taste.getGenre(), taste.getLikeFood(), taste.getDislikeFood(),
                taste.getDietaryPreference(), taste.getSpicyLevel());
    }

    @Override
    @Transactional
    public TasteResponseDto updateLikeFood(Long memberId, String likeFood) {

        Member member = memberService.findById(memberId);
        Optional<Taste> optionalTaste = tasteRepository.findByMember(member);

        Taste taste = optionalTaste.orElse(new Taste(null, null, null, null, null, member));

        taste.updateLikeFood(likeFood);
        tasteRepository.save(taste);

        return new TasteResponseDto(taste.getGenre(), taste.getLikeFood(), taste.getDislikeFood(),
                taste.getDietaryPreference(), taste.getSpicyLevel());
    }

    @Override
    @Transactional
    public TasteResponseDto updateDislikeFood(Long memberId, String dislikeFood) {

        Member member = memberService.findById(memberId);
        Optional<Taste> optionalTaste = tasteRepository.findByMember(member);

        Taste taste = optionalTaste.orElse(new Taste(null, null, null, null, null, member));

        taste.updateDisLikeFood(dislikeFood);
        tasteRepository.save(taste);

        return new TasteResponseDto(taste.getGenre(), taste.getLikeFood(), taste.getDislikeFood(),
                taste.getDietaryPreference(), taste.getSpicyLevel());
    }

    @Override
    @Transactional
    public TasteResponseDto updateDietaryPreference(Long memberId, String dietaryPreference) {

        Member member = memberService.findById(memberId);
        Optional<Taste> optionalTaste = tasteRepository.findByMember(member);

        Taste taste = optionalTaste.orElse(new Taste(null, null, null, null, null, member));

        taste.updateDietaryPreference(dietaryPreference);
        tasteRepository.save(taste);

        return new TasteResponseDto(taste.getGenre(), taste.getLikeFood(), taste.getDislikeFood(),
                taste.getDietaryPreference(), taste.getSpicyLevel());
    }

    @Override
    @Transactional
    public TasteResponseDto updateSpicyLevel(Long memberId, Integer spicyLevel) {

        Member member = memberService.findById(memberId);
        Optional<Taste> optionalTaste = tasteRepository.findByMember(member);

        Taste taste = optionalTaste.orElse(new Taste(null, null, null, null, null, member));

        taste.updateSpicyLevel(spicyLevel);
        tasteRepository.save(taste);

        return new TasteResponseDto(taste.getGenre(), taste.getLikeFood(), taste.getDislikeFood(),
                taste.getDietaryPreference(), taste.getSpicyLevel());
    }

    @Override
    @Transactional(readOnly = true)
    public TasteResponseDto getGenre(Long memberId) {

        Member member = memberService.findById(memberId);
        Optional<Taste> optionalTaste = tasteRepository.findByMember(member);

        Taste taste = optionalTaste.orElse(new Taste("", "", "", "", null, member));

        return new TasteResponseDto(
                taste.getGenre() != null ? taste.getGenre() : "",
                taste.getLikeFood() != null ? taste.getLikeFood() : "",
                taste.getDislikeFood() != null ? taste.getDislikeFood() : "",
                taste.getDietaryPreference() != null ? taste.getDietaryPreference() : "",
                taste.getSpicyLevel() != null ? taste.getSpicyLevel() : null
        );
    }

    @Override
    @Transactional(readOnly = true)
    public TasteResponseDto getLikeFood(Long memberId) {

        Member member = memberService.findById(memberId);
        Optional<Taste> optionalTaste = tasteRepository.findByMember(member);

        Taste taste = optionalTaste.orElse(new Taste("", "", "", "", null, member));

        return new TasteResponseDto(
                taste.getGenre() != null ? taste.getGenre() : "",
                taste.getLikeFood() != null ? taste.getLikeFood() : "",
                taste.getDislikeFood() != null ? taste.getDislikeFood() : "",
                taste.getDietaryPreference() != null ? taste.getDietaryPreference() : "",
                taste.getSpicyLevel() != null ? taste.getSpicyLevel() : null
        );
    }

    @Override
    @Transactional(readOnly = true)
    public TasteResponseDto getDislikeFood(Long memberId) {

        Member member = memberService.findById(memberId);
        Optional<Taste> optionalTaste = tasteRepository.findByMember(member);

        Taste taste = optionalTaste.orElse(new Taste("", "", "", "", null, member));

        return new TasteResponseDto(
                taste.getGenre() != null ? taste.getGenre() : "",
                taste.getLikeFood() != null ? taste.getLikeFood() : "",
                taste.getDislikeFood() != null ? taste.getDislikeFood() : "",
                taste.getDietaryPreference() != null ? taste.getDietaryPreference() : "",
                taste.getSpicyLevel() != null ? taste.getSpicyLevel() : null
        );
    }

    @Override
    @Transactional(readOnly = true)
    public TasteResponseDto getDietaryPreference(Long memberId) {

        Member member = memberService.findById(memberId);
        Optional<Taste> optionalTaste = tasteRepository.findByMember(member);

        Taste taste = optionalTaste.orElse(new Taste("", "", "", "", null, member));

        return new TasteResponseDto(
                taste.getGenre() != null ? taste.getGenre() : "",
                taste.getLikeFood() != null ? taste.getLikeFood() : "",
                taste.getDislikeFood() != null ? taste.getDislikeFood() : "",
                taste.getDietaryPreference() != null ? taste.getDietaryPreference() : "",
                taste.getSpicyLevel() != null ? taste.getSpicyLevel() : null
        );
    }

    @Override
    @Transactional(readOnly = true)
    public TasteResponseDto getSpicyLevel(Long memberId) {

        Member member = memberService.findById(memberId);
        Optional<Taste> optionalTaste = tasteRepository.findByMember(member);

        Taste taste = optionalTaste.orElse(new Taste("", "", "", "", null, member));

        return new TasteResponseDto(
                taste.getGenre() != null ? taste.getGenre() : "",
                taste.getLikeFood() != null ? taste.getLikeFood() : "",
                taste.getDislikeFood() != null ? taste.getDislikeFood() : "",
                taste.getDietaryPreference() != null ? taste.getDietaryPreference() : "",
                taste.getSpicyLevel() != null ? taste.getSpicyLevel() : null
        );
    }
}
