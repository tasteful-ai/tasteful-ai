//package com.example.tastefulai.domain.aichat.controller;
//
//import com.example.tastefulai.domain.aichat.dto.AiChatRequestDto;
//import com.example.tastefulai.domain.aichat.dto.AiChatResponseDto;
//import com.example.tastefulai.domain.aichat.service.AiChatService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/users/aiChats/recommend")
//public class AiController {
//
//    private final AiChatService aiChatService;
//
//    @PostMapping // ("/recommend")
//    public ResponseEntity<AiChatResponseDto> createMenuRecommendation(@RequestBody AiChatRequestDto aiChatRequestDto) {
//
//        // 일반 회원 권한 인증 로직 추가 예정
////        if (!tokenUserRole(token)) {
////            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
////        }
//
//        AiChatResponseDto aiChatResponseDto = aiChatService.createMenuRecommendation(aiChatRequestDto);
//
//        return ResponseEntity.ok(aiChatResponseDto);
//    }
//}
