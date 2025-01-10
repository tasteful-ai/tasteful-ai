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
    public TasteResponseDto updateGenres(Long memberId, String genres) {

        Member member = memberService.findById(memberId);
        Optional<Taste> optionalTaste = tasteRepository.findByMember(member);

        Taste taste = optionalTaste.orElse(new Taste(null, null, null, null, null, member));

        taste.updateGenres(genres);
        tasteRepository.save(taste);

        return new TasteResponseDto(taste.getGenres(), taste.getLikeFoods(), taste.getDislikeFoods(),
                taste.getDietaryPreferences(), taste.getSpicyLevel());
    }

    @Override
    @Transactional
    public TasteResponseDto updateLikeFoods(Long memberId, String likeFoods) {

        Member member = memberService.findById(memberId);
        Optional<Taste> optionalTaste = tasteRepository.findByMember(member);

        Taste taste = optionalTaste.orElse(new Taste(null, null, null, null, null, member));

        taste.updateLikeFoods(likeFoods);
        tasteRepository.save(taste);

        return new TasteResponseDto(taste.getGenres(), taste.getLikeFoods(), taste.getDislikeFoods(),
                taste.getDietaryPreferences(), taste.getSpicyLevel());
    }

    @Override
    @Transactional
    public TasteResponseDto updateDislikeFoods(Long memberId, String dislikeFoods) {

        Member member = memberService.findById(memberId);
        Optional<Taste> optionalTaste = tasteRepository.findByMember(member);

        Taste taste = optionalTaste.orElse(new Taste(null, null, null, null, null, member));

        taste.updateDisLikeFoods(dislikeFoods);
        tasteRepository.save(taste);

        return new TasteResponseDto(taste.getGenres(), taste.getLikeFoods(), taste.getDislikeFoods(),
                taste.getDietaryPreferences(), taste.getSpicyLevel());
    }

    @Override
    @Transactional
    public TasteResponseDto updateDietaryPreferences(Long memberId, String dietaryPreferences) {

        Member member = memberService.findById(memberId);
        Optional<Taste> optionalTaste = tasteRepository.findByMember(member);

        Taste taste = optionalTaste.orElse(new Taste(null, null, null, null, null, member));

        taste.updateDietaryPreferences(dietaryPreferences);
        tasteRepository.save(taste);

        return new TasteResponseDto(taste.getGenres(), taste.getLikeFoods(), taste.getDislikeFoods(),
                taste.getDietaryPreferences(), taste.getSpicyLevel());
    }

    @Override
    @Transactional
    public TasteResponseDto updateSpicyLevel(Long memberId, Integer spicyLevel) {

        Member member = memberService.findById(memberId);
        Optional<Taste> optionalTaste = tasteRepository.findByMember(member);

        Taste taste = optionalTaste.orElse(new Taste(null, null, null, null, null, member));

        taste.updateSpicyLevel(spicyLevel);
        tasteRepository.save(taste);

        return new TasteResponseDto(taste.getGenres(), taste.getLikeFoods(), taste.getDislikeFoods(),
                taste.getDietaryPreferences(), taste.getSpicyLevel());
    }

    @Override
    @Transactional(readOnly = true)
    public TasteResponseDto getGenres(Long memberId) {

        Member member = memberService.findById(memberId);
        Optional<Taste> optionalTaste = tasteRepository.findByMember(member);

        Taste taste = optionalTaste.orElse(new Taste("", "", "", "", null, member));

        return new TasteResponseDto(
                taste.getGenres() != null ? taste.getGenres() : "",
                taste.getLikeFoods() != null ? taste.getLikeFoods() : "",
                taste.getDislikeFoods() != null ? taste.getDislikeFoods() : "",
                taste.getDietaryPreferences() != null ? taste.getDietaryPreferences() : "",
                taste.getSpicyLevel() != null ? taste.getSpicyLevel() : null
        );
    }

    @Override
    @Transactional(readOnly = true)
    public TasteResponseDto getLikeFoods(Long memberId) {

        Member member = memberService.findById(memberId);
        Optional<Taste> optionalTaste = tasteRepository.findByMember(member);

        Taste taste = optionalTaste.orElse(new Taste("", "", "", "", null, member));

        return new TasteResponseDto(
                taste.getGenres() != null ? taste.getGenres() : "",
                taste.getLikeFoods() != null ? taste.getLikeFoods() : "",
                taste.getDislikeFoods() != null ? taste.getDislikeFoods() : "",
                taste.getDietaryPreferences() != null ? taste.getDietaryPreferences() : "",
                taste.getSpicyLevel() != null ? taste.getSpicyLevel() : null
        );
    }

    @Override
    @Transactional(readOnly = true)
    public TasteResponseDto getDislikeFoods(Long memberId) {

        Member member = memberService.findById(memberId);
        Optional<Taste> optionalTaste = tasteRepository.findByMember(member);

        Taste taste = optionalTaste.orElse(new Taste("", "", "", "", null, member));

        return new TasteResponseDto(
                taste.getGenres() != null ? taste.getGenres() : "",
                taste.getLikeFoods() != null ? taste.getLikeFoods() : "",
                taste.getDislikeFoods() != null ? taste.getDislikeFoods() : "",
                taste.getDietaryPreferences() != null ? taste.getDietaryPreferences() : "",
                taste.getSpicyLevel() != null ? taste.getSpicyLevel() : null
        );
    }

    @Override
    @Transactional(readOnly = true)
    public TasteResponseDto getDietaryPreferences(Long memberId) {

        Member member = memberService.findById(memberId);
        Optional<Taste> optionalTaste = tasteRepository.findByMember(member);

        Taste taste = optionalTaste.orElse(new Taste("", "", "", "", null, member));

        return new TasteResponseDto(
                taste.getGenres() != null ? taste.getGenres() : "",
                taste.getLikeFoods() != null ? taste.getLikeFoods() : "",
                taste.getDislikeFoods() != null ? taste.getDislikeFoods() : "",
                taste.getDietaryPreferences() != null ? taste.getDietaryPreferences() : "",
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
                taste.getGenres() != null ? taste.getGenres() : "",
                taste.getLikeFoods() != null ? taste.getLikeFoods() : "",
                taste.getDislikeFoods() != null ? taste.getDislikeFoods() : "",
                taste.getDietaryPreferences() != null ? taste.getDietaryPreferences() : "",
                taste.getSpicyLevel() != null ? taste.getSpicyLevel() : null
        );
    }
}
