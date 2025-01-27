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
import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

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

        List<String> updateGenres = updateTaste(
                member,
                genresRequest,
                genresRepository::findByGenreName,
                Genres::new,
                genres -> tasteGenresRepository.save(new TasteGenres(member, genres))
        );
        return new TasteResponseDto(updateGenres, null, null, null, null);
    }

    @Override
    @Transactional
    public TasteResponseDto updateLikeFoods(Long memberId, List<String> likeFoodsRequest) {

        Member member = memberService.findById(memberId);

        List<String> updateLikeFoods = updateTaste(
                member,
                likeFoodsRequest,
                likeFoodsRepository::findByLikeName,
                LikeFoods::new,
                likeFoods -> tasteLikeFoodsRepository.save(new TasteLikeFoods(member, likeFoods))
        );
        return new TasteResponseDto(null, updateLikeFoods, null, null, null);
    }

    @Override
    @Transactional
    public TasteResponseDto updateDislikeFoods(Long memberId, List<String> dislikeFoodsRequest) {

        Member member = memberService.findById(memberId);

        List<String> updateDislikeFoods = updateTaste(
                member,
                dislikeFoodsRequest,
                dislikeFoodsRepository::findByDislikeName,
                DislikeFoods::new,
                dislikeFoods -> tasteDislikeFoodsRepository.save(new TasteDislikeFoods(member, dislikeFoods))
        );
        return new TasteResponseDto(null, null, updateDislikeFoods, null, null);
    }

    @Override
    @Transactional
    public TasteResponseDto updateDietaryPreferences(Long memberId, List<String> dietaryPreferencesRequest) {

        Member member = memberService.findById(memberId);

        List<String> updateDietaryPreferences = updateTaste(
                member,
                dietaryPreferencesRequest,
                dietaryPreferencesRepository::findByPreferenceName,
                DietaryPreferences::new,
                dietaryPreferences -> tasteDietaryPreferencesRepository.save(new TasteDietaryPreferences(member, dietaryPreferences))
        );
        return new TasteResponseDto(null, null, null, updateDietaryPreferences, null);
    }

    @Override
    @Transactional
    public TasteResponseDto updateSpicyLevel(Long memberId, Integer spicyLevelRequest) {

        Member member = memberService.findById(memberId);

        tasteSpicyLevelRepository.deleteByMember(member);

        SpicyLevel spicyLevel = spicyLevelRepository.findBySpicyLevel(spicyLevelRequest)
                .orElseGet(() -> spicyLevelRepository.save(new SpicyLevel(spicyLevelRequest)));

        TasteSpicyLevel tasteSpicyLevel = new TasteSpicyLevel(member, spicyLevel);
        tasteSpicyLevelRepository.save(tasteSpicyLevel);

        return new TasteResponseDto(null, null, null, null, spicyLevelRequest);
    }

    private <T> List<String> updateTaste(
            Member member,
            List<String> requestList,
            Function<String, Optional<T>> findEntityFunction,
            Function<String, T> createEntityFunction,
            Consumer<T> saveFuntion
    ) {
        if (requestList == null || requestList.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }

        return requestList.stream()
                .distinct()
                .map(name -> findEntityFunction.apply(name).orElseGet(() -> createEntityFunction.apply(name)))
                .peek(saveFuntion)
                .map(Object::toString)
                .toList();
    }
}
