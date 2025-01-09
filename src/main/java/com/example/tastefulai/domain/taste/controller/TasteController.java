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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users/members/{memberId}/tastes")
public class TasteController {

    private final TasteService tasteService;

    @PatchMapping("/genres")
    public ResponseEntity<CommonResponseDto> updateGenre(@RequestHeader("Member-Id") Long memberId,
                                                         @RequestBody TasteRequestDto tasteRequestDto) {

        TasteResponseDto tasteResponseDto = tasteService.updateGenre(memberId, tasteRequestDto.getGenre());

        return new ResponseEntity<>(new CommonResponseDto<>("선호 음식 장르 수정 완료", tasteResponseDto.getGenre()), HttpStatus.OK);
    }

    @PatchMapping("/likeFoods")
    public ResponseEntity<CommonResponseDto> updateLikeFood(@RequestHeader("Member-Id") Long memberId,
                                                            @RequestBody TasteRequestDto tasteRequestDto) {

        TasteResponseDto tasteResponseDto = tasteService.updateLikeFood(memberId, tasteRequestDto.getLikeFood());

        return new ResponseEntity<>(new CommonResponseDto<>("좋아하는 음식 수정 완료", tasteResponseDto.getLikeFood()), HttpStatus.OK);
    }

    @PatchMapping("/dislikeFoods")
    public ResponseEntity<CommonResponseDto> updateDislikeFood(@RequestHeader("Member-Id") Long memberId,
                                                               @RequestBody TasteRequestDto tasteRequestDto) {

        TasteResponseDto tasteResponseDto = tasteService.updateDislikeFood(memberId, tasteRequestDto.getDislikeFood());

        return new ResponseEntity<>(new CommonResponseDto<>("비선호 음식 수정 완료", tasteResponseDto.getDislikeFood()), HttpStatus.OK);
    }

    @PatchMapping("/dietaryPreferences")
    public ResponseEntity<CommonResponseDto> updateDietaryPreference(@RequestHeader("Member-Id") Long memberId,
                                                                     @RequestBody TasteRequestDto tasteRequestDto) {

        TasteResponseDto tasteResponseDto = tasteService.updateDietaryPreference(memberId, tasteRequestDto.getDietaryPreference());

        return new ResponseEntity<>(new CommonResponseDto<>("식단 성향 수정 완료", tasteResponseDto), HttpStatus.OK);
    }

    @PatchMapping("/spicyLevels")
    public ResponseEntity<CommonResponseDto> updateSpicyLevel(@RequestHeader("Member-Id") Long memberId,
                                                              @RequestBody TasteRequestDto tasteRequestDto) {

        TasteResponseDto tasteResponseDto = tasteService.updateSpicyLevel(memberId, tasteRequestDto.getSpicyLevel());

        return new ResponseEntity<>(new CommonResponseDto<>("매운 정도 수정 완료", tasteResponseDto), HttpStatus.OK);
    }

    @GetMapping("/genres")
    public ResponseEntity<CommonResponseDto> getGenre(@RequestHeader("Member-Id") Long memberId) {

        TasteResponseDto tasteResponseDto = tasteService.getGenre(memberId);

        return new ResponseEntity<>(new CommonResponseDto<>("선호 음식 장르 조회 완료", tasteResponseDto.getGenre()), HttpStatus.OK);
    }

    @GetMapping("/likeFoods")
    public ResponseEntity<CommonResponseDto> getLikeFood(@RequestHeader("Member-Id") Long memberId) {

        TasteResponseDto tasteResponseDto = tasteService.getLikeFood(memberId);

        return new ResponseEntity<>(new CommonResponseDto<>("좋아하는 음식 조회 완료", tasteResponseDto.getLikeFood()), HttpStatus.OK);
    }

    @GetMapping("/dislikeFoods")
    public ResponseEntity<CommonResponseDto> getDislikeFood(@RequestHeader("Member-Id") Long memberId) {

        TasteResponseDto tasteResponseDto = tasteService.getDislikeFood(memberId);

        return new ResponseEntity<>(new CommonResponseDto<>("비선호 음식 조회 완료", tasteResponseDto.getDislikeFood()), HttpStatus.OK);
    }

    @GetMapping("/dietaryPreferences")
    public ResponseEntity<CommonResponseDto> getDietaryPreference(@RequestHeader("Member-Id") Long memberId) {

        TasteResponseDto tasteResponseDto = tasteService.getDietaryPreference(memberId);

        return new ResponseEntity<>(new CommonResponseDto<>("식단 성향 조회 완료", tasteResponseDto.getDietaryPreference()), HttpStatus.OK);
    }

    @GetMapping("/spicyLevels")
    public ResponseEntity<CommonResponseDto> getSpicyLevel(@RequestHeader("Member-Id") Long memberId) {

        TasteResponseDto tasteResponseDto = tasteService.getSpicyLevel(memberId);

        return new ResponseEntity<>(new CommonResponseDto<>("매운 정도 조회 완료", tasteResponseDto.getSpicyLevel()), HttpStatus.OK);
    }
}