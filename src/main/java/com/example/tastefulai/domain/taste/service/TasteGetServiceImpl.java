package com.example.tastefulai.domain.taste.service;

import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.member.service.MemberService;
import com.example.tastefulai.domain.taste.dto.TasteResponseDto;
import com.example.tastefulai.domain.taste.entity.spicylevel.TasteSpicyLevel;
import com.example.tastefulai.domain.taste.repository.dietarypreferences.TasteDietaryPreferencesRepository;
import com.example.tastefulai.domain.taste.repository.dislikefoods.TasteDislikeFoodsRepository;
import com.example.tastefulai.domain.taste.repository.genres.TasteGenresRepository;
import com.example.tastefulai.domain.taste.repository.likefoods.TasteLikeFoodsRepository;
import com.example.tastefulai.domain.taste.repository.spicylevel.TasteSpicyLevelRepository;
import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.CustomException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TasteGetServiceImpl implements TasteGetService {

    private final MemberService memberService;
    private final TasteGenresRepository tasteGenresRepository;
    private final TasteLikeFoodsRepository tasteLikeFoodsRepository;
    private final TasteDislikeFoodsRepository tasteDislikeFoodsRepository;
    private final TasteDietaryPreferencesRepository tasteDietaryPreferencesRepository;
    private final TasteSpicyLevelRepository tasteSpicyLevelRepository;

    @Override
    @Transactional(readOnly = true)
    public TasteResponseDto getGenres(Long memberId) {

        Member member = memberService.findById(memberId);
        List<String> genres = tasteGenresRepository.findByMember(member).stream()
                .map(tg -> tg.getGenres().getGenreName())
                .collect(Collectors.toList());

        return new TasteResponseDto(genres, null, null,null, null);
    }

    @Override
    @Transactional(readOnly = true)
    public TasteResponseDto getLikeFoods(Long memberId) {

        Member member = memberService.findById(memberId);
        List<String> likeFoods = tasteLikeFoodsRepository.findByMember(member).stream()
                .map(tg -> tg.getLikeFoods().getLikeName())
                .collect(Collectors.toList());

        return new TasteResponseDto(null, likeFoods, null,null, null);
    }

    @Override
    @Transactional(readOnly = true)
    public TasteResponseDto getDislikeFoods(Long memberId) {

        Member member = memberService.findById(memberId);
        List<String> dislikeFoods = tasteDislikeFoodsRepository.findByMember(member).stream()
                .map(tg -> tg.getDislikeFoods().getDislikeName())
                .collect(Collectors.toList());

        return new TasteResponseDto(null, null, dislikeFoods,null, null);
    }

    @Override
    @Transactional(readOnly = true)
    public TasteResponseDto getDietaryPreferences(Long memberId) {

        Member member = memberService.findById(memberId);
        List<String> dietaryPreferences = tasteDietaryPreferencesRepository.findByMember(member).stream()
                .map(tg -> tg.getDietaryPreferences().getPreferenceName())
                .collect(Collectors.toList());

        return new TasteResponseDto(null, null, null, dietaryPreferences, null);
    }

    @Override
    @Transactional(readOnly = true)
    public TasteResponseDto getSpicyLevel(Long memberId) {

        Member member = memberService.findById(memberId);
        TasteSpicyLevel tasteSpicyLevel = tasteSpicyLevelRepository.findByMember(member)
                .orElseThrow(() -> new CustomException(ErrorCode.DATA_NOT_FOUND));

        Integer spicyLevel = tasteSpicyLevel.getSpicyLevel().getSpicyLevel();

        return new TasteResponseDto(null, null, null,null, spicyLevel);
    }
}
