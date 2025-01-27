package com.example.tastefulai.domain.taste.controller;

import com.example.tastefulai.domain.taste.dto.TasteRequestDto;
import com.example.tastefulai.domain.taste.dto.TasteResponseDto;
import com.example.tastefulai.domain.taste.service.TasteGetService;
import com.example.tastefulai.domain.taste.service.TasteUpdateService;
import com.example.tastefulai.global.common.dto.CommonResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/members/{memberId}/tastes")
public class TasteController {

    private final TasteUpdateService tasteUpdateService;
    private final TasteGetService tasteGetService;

    @PatchMapping("/genres")
    public ResponseEntity<CommonResponseDto<List<String>>> updateGenres(@PathVariable Long memberId,
                                                                        @RequestBody @Valid TasteRequestDto tasteRequestDto) {

        TasteResponseDto tasteResponseDto = tasteUpdateService.updateGenres(memberId, tasteRequestDto.getGenres());

        return new ResponseEntity<>(new CommonResponseDto<>("선호 음식 장르 수정 완료", tasteResponseDto.getGenres()), HttpStatus.OK);
    }

    @PatchMapping("/likeFoods")
    public ResponseEntity<CommonResponseDto<List<String>>> updateLikeFoods(@PathVariable Long memberId,
                                                                           @RequestBody @Valid TasteRequestDto tasteRequestDto) {

        TasteResponseDto tasteResponseDto = tasteUpdateService.updateLikeFoods(memberId, tasteRequestDto.getLikeFoods());

        return new ResponseEntity<>(new CommonResponseDto<>("좋아하는 음식 수정 완료", tasteResponseDto.getLikeFoods()), HttpStatus.OK);
    }

    @PatchMapping("/dislikeFoods")
    public ResponseEntity<CommonResponseDto<List<String>>> updateDislikeFoods(@PathVariable Long memberId,
                                                                              @RequestBody @Valid TasteRequestDto tasteRequestDto) {

        TasteResponseDto tasteResponseDto = tasteUpdateService.updateDislikeFoods(memberId, tasteRequestDto.getDislikeFoods());

        return new ResponseEntity<>(new CommonResponseDto<>("비선호 음식 수정 완료", tasteResponseDto.getDislikeFoods()), HttpStatus.OK);
    }

    @PatchMapping("/dietaryPreferences")
    public ResponseEntity<CommonResponseDto<List<String>>> updateDietaryPreferences(@PathVariable Long memberId,
                                                                                    @RequestBody @Valid TasteRequestDto tasteRequestDto) {

        TasteResponseDto tasteResponseDto = tasteUpdateService.updateDietaryPreferences(memberId, tasteRequestDto.getDietaryPreferences());

        return new ResponseEntity<>(new CommonResponseDto<>("식단 성향 수정 완료", tasteResponseDto.getDietaryPreferences()), HttpStatus.OK);
    }

    @PatchMapping("/spicyLevels")
    public ResponseEntity<CommonResponseDto<Integer>> updateSpicyLevel(@PathVariable Long memberId,
                                                                       @RequestBody @Valid TasteRequestDto tasteRequestDto) {

        TasteResponseDto tasteResponseDto = tasteUpdateService.updateSpicyLevel(memberId, tasteRequestDto.getSpicyLevel());

        return new ResponseEntity<>(new CommonResponseDto<>("매운 정도 수정 완료", tasteResponseDto.getSpicyLevel()), HttpStatus.OK);
    }

    @GetMapping("/genres")
    public ResponseEntity<CommonResponseDto<List<String>>> getGenres(@PathVariable Long memberId) {

        TasteResponseDto tasteResponseDto = tasteGetService.getGenres(memberId);

        return ResponseEntity.ok(
                new CommonResponseDto<>("선호 음식 장르 조회 완료", tasteResponseDto.getGenres())
        );
    }

    @GetMapping("/likeFoods")
    public ResponseEntity<CommonResponseDto<List<String>>> getLikeFoods(@PathVariable Long memberId) {

        TasteResponseDto tasteResponseDto = tasteGetService.getLikeFoods(memberId);

        return ResponseEntity.ok(
                new CommonResponseDto<>("좋아하는 음식 조회 완료", tasteResponseDto.getLikeFoods())
        );
    }

    @GetMapping("/dislikeFoods")
    public ResponseEntity<CommonResponseDto<List<String>>> getDislikeFoods(@PathVariable Long memberId) {

        TasteResponseDto tasteResponseDto = tasteGetService.getDislikeFoods(memberId);
        return ResponseEntity.ok(
                new CommonResponseDto<>("비선호 음식 조회 완료", tasteResponseDto.getDislikeFoods())
        );
    }

    @GetMapping("/dietaryPreferences")
    public ResponseEntity<CommonResponseDto<List<String>>> getDietaryPreferences(@PathVariable Long memberId) {

        TasteResponseDto tasteResponseDto = tasteGetService.getDietaryPreferences(memberId);
        return ResponseEntity.ok(
                new CommonResponseDto<>("식단 성향 조회 완료", tasteResponseDto.getDietaryPreferences())
        );
    }

    @GetMapping("/spicyLevels")
    public ResponseEntity<CommonResponseDto<Integer>> getSpicyLevel(@PathVariable Long memberId) {

        TasteResponseDto tasteResponseDto = tasteGetService.getSpicyLevel(memberId);
        return ResponseEntity.ok(
                new CommonResponseDto<>("매운 정도 조회 완료", tasteResponseDto.getSpicyLevel())
        );
    }
}