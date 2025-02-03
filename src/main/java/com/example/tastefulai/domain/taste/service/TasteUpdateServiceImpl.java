package com.example.tastefulai.domain.taste.service;

import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.member.service.MemberService;
import com.example.tastefulai.domain.taste.dto.TasteResponseDto;
import com.example.tastefulai.domain.taste.entity.dietarypreferences.DietaryPreferences;
import com.example.tastefulai.domain.taste.entity.dietarypreferences.TasteDietaryPreferences;
import com.example.tastefulai.domain.taste.entity.dislikefoods.DislikeFoods;
import com.example.tastefulai.domain.taste.entity.dislikefoods.TasteDislikeFoods;
import com.example.tastefulai.domain.taste.entity.genres.Genres;
import com.example.tastefulai.domain.taste.entity.genres.TasteGenres;
import com.example.tastefulai.domain.taste.entity.likefoods.LikeFoods;
import com.example.tastefulai.domain.taste.entity.likefoods.TasteLikeFoods;
import com.example.tastefulai.domain.taste.entity.spicylevel.SpicyLevel;
import com.example.tastefulai.domain.taste.entity.spicylevel.TasteSpicyLevel;
import com.example.tastefulai.domain.taste.repository.dietarypreferences.DietaryPreferencesRepository;
import com.example.tastefulai.domain.taste.repository.dietarypreferences.TasteDietaryPreferencesRepository;
import com.example.tastefulai.domain.taste.repository.dislikefoods.DislikeFoodsRepository;
import com.example.tastefulai.domain.taste.repository.dislikefoods.TasteDislikeFoodsRepository;
import com.example.tastefulai.domain.taste.repository.genres.GenresRepository;
import com.example.tastefulai.domain.taste.repository.genres.TasteGenresRepository;
import com.example.tastefulai.domain.taste.repository.likefoods.LikeFoodsRepository;
import com.example.tastefulai.domain.taste.repository.likefoods.TasteLikeFoodsRepository;
import com.example.tastefulai.domain.taste.repository.spicylevel.SpicyLevelRepository;
import com.example.tastefulai.domain.taste.repository.spicylevel.TasteSpicyLevelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TasteUpdateServiceImpl implements TasteUpdateService {

    private final MemberService memberService;
    private final GenresRepository genresRepository;
    private final TasteGenresRepository tasteGenresRepository;
    private final LikeFoodsRepository likeFoodsRepository;
    private final TasteLikeFoodsRepository tasteLikeFoodsRepository;
    private final DislikeFoodsRepository dislikeFoodsRepository;
    private final TasteDislikeFoodsRepository tasteDislikeFoodsRepository;
    private final DietaryPreferencesRepository dietaryPreferencesRepository;
    private final TasteDietaryPreferencesRepository tasteDietaryPreferencesRepository;
    private final SpicyLevelRepository spicyLevelRepository;
    private final TasteSpicyLevelRepository tasteSpicyLevelRepository;

    @Override
    @Transactional
    public TasteResponseDto updateGenres(Long memberId, List<String> genresRequest) {

        Member member = memberService.findById(memberId);

        // 기존 데이터 삭제
        tasteGenresRepository.deleteByMember(member);

        // 새 데이터 저장
        List<Genres> genresList = genresRequest.stream()
                .distinct() // 중복 제거 (이미 Controller에서 검증되었지만 추가 안전망)
                .map(genreName -> genresRepository.findByGenreName(genreName)
                        .orElseGet(() -> genresRepository.save(new Genres(genreName))))
                .toList();

        genresList.forEach(genres -> tasteGenresRepository.save(new TasteGenres(member, genres)));

        // 저장된 장르 이름 반환
        List<String> updateGenres = genresList.stream()
                .map(Genres::getGenreName)
                .toList();

        return new TasteResponseDto(updateGenres, null, null, null, null);
    }


    @Override
    @Transactional
    public TasteResponseDto updateLikeFoods(Long memberId, List<String> likeFoodsRequest) {

        Member member = memberService.findById(memberId);

        tasteLikeFoodsRepository.deleteByMember(member);

        List<LikeFoods> likeFoodsList = likeFoodsRequest.stream()
                .distinct()
                .map(likeName -> likeFoodsRepository.findByLikeName(likeName)
                        .orElseGet(() -> likeFoodsRepository.save(new LikeFoods(likeName))))
                .toList();

        likeFoodsList.forEach(likeFoods -> tasteLikeFoodsRepository.save(new TasteLikeFoods(member, likeFoods)));

        List<String> updateLikeFoods = likeFoodsList.stream()
                .map(LikeFoods::getLikeName)
                .toList();

        return new TasteResponseDto(null, updateLikeFoods, null, null, null);
    }

    @Override
    @Transactional
    public TasteResponseDto updateDislikeFoods(Long memberId, List<String> dislikeFoodsRequest) {

        Member member = memberService.findById(memberId);

        tasteDislikeFoodsRepository.deleteByMember(member);

        List<DislikeFoods> dislikeFoodsList = dislikeFoodsRequest.stream()
                .distinct()
                .map(dislikeName -> dislikeFoodsRepository.findByDislikeName(dislikeName)
                        .orElseGet(() -> dislikeFoodsRepository.save(new DislikeFoods(dislikeName))))
                .toList();

        dislikeFoodsList.forEach(dislikeFoods -> tasteDislikeFoodsRepository.save(new TasteDislikeFoods(member, dislikeFoods)));

        List<String> updateDislikeFoods = dislikeFoodsList.stream()
                .map(DislikeFoods::getDislikeName)
                .toList();

        return new TasteResponseDto(null, null, updateDislikeFoods, null, null);
    }

    @Override
    @Transactional
    public TasteResponseDto updateDietaryPreferences(Long memberId, List<String> dietaryPreferencesRequest) {

        Member member = memberService.findById(memberId);

        tasteDietaryPreferencesRepository.deleteByMember(member);

        List<DietaryPreferences> dietaryPreferencesList = dietaryPreferencesRequest.stream()
                .distinct()
                .map(preferenceName -> dietaryPreferencesRepository.findByPreferenceName(preferenceName)
                        .orElseGet(() -> dietaryPreferencesRepository.save(new DietaryPreferences(preferenceName))))
                .toList();

        dietaryPreferencesList.forEach(dietaryPreferences -> tasteDietaryPreferencesRepository.save(new TasteDietaryPreferences(member, dietaryPreferences)));

        List<String> updateDietaryPreferences = dietaryPreferencesList.stream()
                .map(DietaryPreferences::getPreferenceName)
                .toList();

        return new TasteResponseDto(null, null, null, updateDietaryPreferences, null);
    }

    @Override
    @Transactional
    public TasteResponseDto updateSpicyLevel(Long memberId, List<Integer> spicyLevelRequest) {
        Member member = memberService.findById(memberId);

        tasteSpicyLevelRepository.deleteByMember(member);

        List<TasteSpicyLevel> spicyLevelList = spicyLevelRequest.stream()
                .distinct()
                .map(level -> {
                    SpicyLevel spicyLevel =  spicyLevelRepository.findBySpicyLevel(level)
                            .orElseGet(() -> spicyLevelRepository.save(new SpicyLevel(level)));

                    return new TasteSpicyLevel(member, spicyLevel);
                })
                .toList();

        tasteSpicyLevelRepository.saveAll(spicyLevelList);

        return new TasteResponseDto(null, null, null, null, spicyLevelRequest);
    }
}
