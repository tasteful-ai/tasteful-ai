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
    /**
     * 회원의 선호 장르를 수정
     *
     * <p>기존에 저장된 회원의 장르 정보를 삭제한 후, 새로운 장르 목록을 저장
     * 만약 요청된 장르가 데이터베이스에 존재하지 않으면, 새로운 장르를 생성하여 저장
     *
     * @param memberId 장르 정보를 수정할 회원의 ID
     * @param genresRequest 회원이 선호하는 장르 목록
     * @return 업데이트된 장르 목록을 포함하는 {@link TasteResponseDto}
     */
    public TasteResponseDto updateGenres(Long memberId, List<String> genresRequest) {

        Member member = memberService.findById(memberId);
        tasteGenresRepository.deleteByMember(member);

        List<Genres> genresList = genresRequest.stream()
                .distinct()
                .map(genreName -> genresRepository.findByGenreName(genreName)
                        .orElseGet(() -> genresRepository.save(new Genres(genreName))))
                .toList();

        List<TasteGenres> tasteGenresList = genresList.stream()
                .map(genres -> new TasteGenres(member, genres))
                .toList();

        tasteGenresRepository.saveAll(tasteGenresList);

        return new TasteResponseDto(genresList.stream().map(Genres::getGenreName).toList(), null, null, null, null);
    }

    /**
     * 회원이 좋아하는 음식을 수정
     *
     * <p>기존 데이터를 삭제한 후 새로운 음식 목록을 저장
     * 만약 요청된 음식이 데이터베이스에 존재하지 않으면, 새로운 데이터를 생성하여 저장
     *
     * @param memberId 좋아하는 음식 정보를 수정할 회원의 ID
     * @param likeFoodsRequest 회원이 좋아하는 음식 목록
     * @return 업데이트된 좋아하는 음식 목록을 포함하는 {@link TasteResponseDto}
     */
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

        List<TasteLikeFoods> tasteLikeFoodsList = likeFoodsList.stream()
                .map(likeFoods -> new TasteLikeFoods(member, likeFoods))
                .toList();

        tasteLikeFoodsRepository.saveAll(tasteLikeFoodsList);

        return new TasteResponseDto(null, likeFoodsList.stream().map(LikeFoods::getLikeName).toList(), null, null, null);
    }

    /**
     * 회원이 싫어하는 음식을 수정
     *
     * <p>기존 데이터를 삭제한 후 새로운 음식 목록을 저장
     * 만약 요청된 음식이 데이터베이스에 존재하지 않으면, 새로운 데이터를 생성하여 저장
     *
     * @param memberId 싫어하는 음식 정보를 수정할 회원의 ID
     * @param dislikeFoodsRequest 회원이 싫어하는 음식 목록
     * @return 업데이트된 싫어하는 음식 목록을 포함하는 {@link TasteResponseDto}
     */
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

        List<TasteDislikeFoods> tasteDislikeFoodsList = dislikeFoodsList.stream()
                .map(dislikeFoods -> new TasteDislikeFoods(member, dislikeFoods))
                .toList();

        tasteDislikeFoodsRepository.saveAll(tasteDislikeFoodsList);

        return new TasteResponseDto(null, null, dislikeFoodsList.stream().map(DislikeFoods::getDislikeName).toList(),null, null);
    }

    /**
     * 회원의 식단 선호도를 수정
     *
     * <p>기존 데이터를 삭제한 후 새로운 식단 선호도를 저장
     * 만약 요청된 식단이 데이터베이스에 존재하지 않으면, 새로운 데이터를 생성하여 저장
     *
     * @param memberId 식단 선호도를 수정할 회원의 ID
     * @param dietaryPreferencesRequest 회원이 선호하는 식단 목록
     * @return 업데이트된 식단 선호도를 포함하는 {@link TasteResponseDto}
     */
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

        List<TasteDietaryPreferences> tasteDietaryPreferencesList = dietaryPreferencesList.stream()
                .map(dietaryPreferences -> new TasteDietaryPreferences(member, dietaryPreferences))
                .toList();

        tasteDietaryPreferencesRepository.saveAll(tasteDietaryPreferencesList);

        return new TasteResponseDto(null, null,null, dietaryPreferencesList.stream().map(DietaryPreferences::getPreferenceName).toList(),null);
    }


    /**
     * 회원의 매운맛 선호도를 수정
     *
     * <p>기존 데이터를 삭제한 후 새로운 매운맛 선호도를 저장
     * 만약 요청된 매운맛 단계가 데이터베이스에 존재하지 않으면, 새로운 데이터를 생성하여 저장
     *
     * @param memberId 매운맛 선호도를 수정할 회원의 ID
     * @param spicyLevelRequest 회원이 선호하는 매운맛 단계 (정수 값)
     * @return 업데이트된 매운맛 단계를 포함하는 {@link TasteResponseDto}
     */
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
}
