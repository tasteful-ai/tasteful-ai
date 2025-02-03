package com.example.tastefulai.domain.taste.controller;

import com.example.tastefulai.domain.taste.dto.TasteRequestDto;
import com.example.tastefulai.domain.taste.dto.TasteResponseDto;
import com.example.tastefulai.domain.taste.service.TasteGetService;
import com.example.tastefulai.domain.taste.service.TasteUpdateService;
import com.example.tastefulai.global.common.dto.CommonResponseDto;
import com.example.tastefulai.global.config.auth.MemberDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members/{memberId}/tastes")
public class TasteController {

    private final TasteUpdateService tasteUpdateService;
    private final TasteGetService tasteGetService;

    @PatchMapping("/genres")
    public ResponseEntity<CommonResponseDto<List<String>>> updateGenres(@AuthenticationPrincipal MemberDetailsImpl memberDetails,
                                                                        @Valid @RequestBody TasteRequestDto tasteRequestDto) {

        Long memberId = memberDetails.getId();

        TasteResponseDto tasteResponseDto = tasteUpdateService.updateGenres(memberId, tasteRequestDto.getGenres());

        return new ResponseEntity<>(new CommonResponseDto<>("선호 음식 장르 수정 완료", tasteResponseDto.getGenres()), HttpStatus.OK);
    }

    @PatchMapping("/likeFoods")
    public ResponseEntity<CommonResponseDto<List<String>>> updateLikeFoods(@AuthenticationPrincipal MemberDetailsImpl memberDetails,
                                                                           @Valid @RequestBody TasteRequestDto tasteRequestDto) {

        Long memberId = memberDetails.getId();

        TasteResponseDto tasteResponseDto = tasteUpdateService.updateLikeFoods(memberId, tasteRequestDto.getLikeFoods());

        return new ResponseEntity<>(new CommonResponseDto<>("좋아하는 음식 수정 완료", tasteResponseDto.getLikeFoods()), HttpStatus.OK);
    }

    @PatchMapping("/dislikeFoods")
    public ResponseEntity<CommonResponseDto<List<String>>> updateDislikeFoods(@AuthenticationPrincipal MemberDetailsImpl memberDetails,
                                                                              @Valid @RequestBody TasteRequestDto tasteRequestDto) {

        Long memberId = memberDetails.getId();

        TasteResponseDto tasteResponseDto = tasteUpdateService.updateDislikeFoods(memberId, tasteRequestDto.getDislikeFoods());

        return new ResponseEntity<>(new CommonResponseDto<>("비선호 음식 수정 완료", tasteResponseDto.getDislikeFoods()), HttpStatus.OK);
    }

    @PatchMapping("/dietaryPreferences")
    public ResponseEntity<CommonResponseDto<List<String>>> updateDietaryPreferences(@AuthenticationPrincipal MemberDetailsImpl memberDetails,
                                                                                    @Valid @RequestBody TasteRequestDto tasteRequestDto) {
        Long memberId = memberDetails.getId();

        TasteResponseDto tasteResponseDto = tasteUpdateService.updateDietaryPreferences(memberId, tasteRequestDto.getDietaryPreferences());

        return new ResponseEntity<>(new CommonResponseDto<>("식단 성향 수정 완료", tasteResponseDto.getDietaryPreferences()), HttpStatus.OK);
    }

    @PatchMapping("/spicyLevel")
    public ResponseEntity<CommonResponseDto<List<Integer>>> updateSpicyLevel(@AuthenticationPrincipal MemberDetailsImpl memberDetails,
                                                                       @Valid @RequestBody TasteRequestDto tasteRequestDto) {
        Long memberId = memberDetails.getId();

        TasteResponseDto tasteResponseDto = tasteUpdateService.updateSpicyLevel(memberId, tasteRequestDto.getSpicyLevel());

        return new ResponseEntity<>(new CommonResponseDto<>("매운 정도 수정 완료", tasteResponseDto.getSpicyLevel()), HttpStatus.OK);
    }

    @GetMapping("/genres")
    public ResponseEntity<CommonResponseDto<List<String>>> getGenres(@AuthenticationPrincipal MemberDetailsImpl memberDetails) {

        Long memberId = memberDetails.getId();

        TasteResponseDto tasteResponseDto = tasteGetService.getGenres(memberId);

        return ResponseEntity.ok(
                new CommonResponseDto<>("선호 음식 장르 조회 완료", tasteResponseDto.getGenres())
        );
    }

    @GetMapping("/likeFoods")
    public ResponseEntity<CommonResponseDto<List<String>>> getLikeFoods(@AuthenticationPrincipal MemberDetailsImpl memberDetails) {

        Long memberId = memberDetails.getId();

        TasteResponseDto tasteResponseDto = tasteGetService.getLikeFoods(memberId);

        return ResponseEntity.ok(
                new CommonResponseDto<>("좋아하는 음식 조회 완료", tasteResponseDto.getLikeFoods())
        );
    }

    @GetMapping("/dislikeFoods")
    public ResponseEntity<CommonResponseDto<List<String>>> getDislikeFoods(@AuthenticationPrincipal MemberDetailsImpl memberDetails) {

        Long memberId = memberDetails.getId();

        TasteResponseDto tasteResponseDto = tasteGetService.getDislikeFoods(memberId);
        return ResponseEntity.ok(
                new CommonResponseDto<>("비선호 음식 조회 완료", tasteResponseDto.getDislikeFoods())
        );
    }

    @GetMapping("/dietaryPreferences")
    public ResponseEntity<CommonResponseDto<List<String>>> getDietaryPreferences(@AuthenticationPrincipal MemberDetailsImpl memberDetails) {

        Long memberId = memberDetails.getId();

        TasteResponseDto tasteResponseDto = tasteGetService.getDietaryPreferences(memberId);
        return ResponseEntity.ok(
                new CommonResponseDto<>("식단 성향 조회 완료", tasteResponseDto.getDietaryPreferences())
        );
    }

    @GetMapping("/spicyLevel")
    public ResponseEntity<CommonResponseDto<List<Integer>>> getSpicyLevel(@AuthenticationPrincipal MemberDetailsImpl memberDetails) {

        Long memberId = memberDetails.getId();

        TasteResponseDto tasteResponseDto = tasteGetService.getSpicyLevel(memberId);
        return ResponseEntity.ok(
                new CommonResponseDto<>("매운 정도 조회 완료", tasteResponseDto.getSpicyLevel())
        );
    }
}