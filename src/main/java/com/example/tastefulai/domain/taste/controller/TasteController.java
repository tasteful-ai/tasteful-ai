package com.example.tastefulai.domain.taste.controller;

import com.example.tastefulai.domain.taste.dto.TasteRequestDto;
import com.example.tastefulai.domain.taste.dto.TasteResponseDto;
import com.example.tastefulai.domain.taste.service.TasteService;
import com.example.tastefulai.global.common.dto.CommonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/members/{memberId}/tastes")
public class TasteController {

    private final TasteService tasteService;

    @PatchMapping("/genres")
    public ResponseEntity<CommonResponseDto<String>> updateGenres(@PathVariable Long memberId,
                                                         @RequestBody TasteRequestDto tasteRequestDto) {

        TasteResponseDto tasteResponseDto = tasteService.updateGenres(memberId, tasteRequestDto.getGenres());

        return new ResponseEntity<>(new CommonResponseDto<>("선호 음식 장르 수정 완료", tasteResponseDto.getGenres()), HttpStatus.OK);
    }

    @PatchMapping("/likeFoods")
    public ResponseEntity<CommonResponseDto<String>> updateLikeFoods(@PathVariable Long memberId,
                                                            @RequestBody TasteRequestDto tasteRequestDto) {

        TasteResponseDto tasteResponseDto = tasteService.updateLikeFoods(memberId, tasteRequestDto.getLikeFoods());

        return new ResponseEntity<>(new CommonResponseDto<>("좋아하는 음식 수정 완료", tasteResponseDto.getLikeFoods()), HttpStatus.OK);
    }

    @PatchMapping("/dislikeFoods")
    public ResponseEntity<CommonResponseDto<String>> updateDislikeFoods(@PathVariable Long memberId,
                                                               @RequestBody TasteRequestDto tasteRequestDto) {

        TasteResponseDto tasteResponseDto = tasteService.updateDislikeFoods(memberId, tasteRequestDto.getDislikeFoods());

        return new ResponseEntity<>(new CommonResponseDto<>("비선호 음식 수정 완료", tasteResponseDto.getDislikeFoods()), HttpStatus.OK);
    }

    @PatchMapping("/dietaryPreferences")
    public ResponseEntity<CommonResponseDto<TasteResponseDto>> updateDietaryPreferences(@PathVariable Long memberId,
                                                                     @RequestBody TasteRequestDto tasteRequestDto) {

        TasteResponseDto tasteResponseDto = tasteService.updateDietaryPreferences(memberId, tasteRequestDto.getDietaryPreferences());

        return new ResponseEntity<>(new CommonResponseDto<>("식단 성향 수정 완료", tasteResponseDto), HttpStatus.OK);
    }

    @PatchMapping("/spicyLevels")
    public ResponseEntity<CommonResponseDto<TasteResponseDto>> updateSpicyLevel(@PathVariable Long memberId,
                                                              @RequestBody TasteRequestDto tasteRequestDto) {

        TasteResponseDto tasteResponseDto = tasteService.updateSpicyLevel(memberId, tasteRequestDto.getSpicyLevel());

        return new ResponseEntity<>(new CommonResponseDto<>("매운 정도 수정 완료", tasteResponseDto), HttpStatus.OK);
    }

    @GetMapping("/genres")
    public ResponseEntity<CommonResponseDto<String>> getGenres(@PathVariable Long memberId) {

        TasteResponseDto tasteResponseDto = tasteService.getGenres(memberId);

        return new ResponseEntity<>(new CommonResponseDto<>("선호 음식 장르 조회 완료", tasteResponseDto.getGenres()), HttpStatus.OK);
    }

    @GetMapping("/likeFoods")
    public ResponseEntity<CommonResponseDto<String>> getLikeFoods(@PathVariable Long memberId) {

        TasteResponseDto tasteResponseDto = tasteService.getLikeFoods(memberId);

        return new ResponseEntity<>(new CommonResponseDto<>("좋아하는 음식 조회 완료", tasteResponseDto.getLikeFoods()), HttpStatus.OK);
    }

    @GetMapping("/dislikeFoods")
    public ResponseEntity<CommonResponseDto<String>> getDislikeFoods(@PathVariable Long memberId) {

        TasteResponseDto tasteResponseDto = tasteService.getDislikeFoods(memberId);

        return new ResponseEntity<>(new CommonResponseDto<>("비선호 음식 조회 완료", tasteResponseDto.getDislikeFoods()), HttpStatus.OK);
    }

    @GetMapping("/dietaryPreferences")
    public ResponseEntity<CommonResponseDto<String>> getDietaryPreferences(@PathVariable Long memberId) {

        TasteResponseDto tasteResponseDto = tasteService.getDietaryPreferences(memberId);

        return new ResponseEntity<>(new CommonResponseDto<>("식단 성향 조회 완료", tasteResponseDto.getDietaryPreferences()), HttpStatus.OK);
    }

    @GetMapping("/spicyLevels")
    public ResponseEntity<CommonResponseDto<Integer>> getSpicyLevel(@PathVariable Long memberId) {

        TasteResponseDto tasteResponseDto = tasteService.getSpicyLevel(memberId);

        return new ResponseEntity<>(new CommonResponseDto<>("매운 정도 조회 완료", tasteResponseDto.getSpicyLevel()), HttpStatus.OK);
    }
}