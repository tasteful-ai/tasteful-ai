//package com.example.tastefulai.domain.aichat.service;
//
//import com.example.tastefulai.domain.aichat.dto.AiChatRequestDto;
//import com.example.tastefulai.domain.aichat.dto.AiChatResponseDto;
////import com.example.tastefulai.global.config.OpenAiConfig;
//import com.example.tastefulai.global.config.OpenAiConfig;
//import lombok.RequiredArgsConstructor;
////import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.BodyInserters;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//public class AiChatServiceImpl implements AiChatService {
//
//    private final OpenAiConfig openAiConfig;
//    private final WebClient webClient = WebClient.builder().build();
////    private final ChatClient chatClient;
//
//    @Override
//    public AiChatResponseDto createMenuRecommendation(AiChatRequestDto aiChatRequestDto) {
//
//
//        // OpenAI API 요청 본문 구성
//        Map<String, Object> requestBody = new HashMap<>();
//        requestBody.put("model", "gpt-3.5-turbo-16k");
//        requestBody.put("messages", new Object[] {
//                Map.of("role", "user", "content", aiChatRequestDto.getMessage())
//        });
//        requestBody.put("max_tokens", 100); // 응답 토큰 수 추후 수정
//
//        // API 호출
//        Mono<Map> response = webClient.post()
//                .uri(openAiConfig.getUrl())
//                .header(HttpHeaders.AUTHORIZATION, "Bearer" + openAiConfig.getKey())
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(BodyInserters.fromValue(requestBody))
//                .retrieve()
//                .bodyToMono(Map.class);
//
//        Map<String, Object> responseMap = response.block();
//
//        // 응답 파싱
//        String recommandation = "";
//
//        if (responseMap != null && responseMap.containsKey("choices")) {
//            var choices = (java.util.List<Map<String, Object>>) responseMap.get("choices");
//            if (!choices.isEmpty()) {
//                var message = (Map<String, Object>) choices.get(0).get("message");
//                recommandation = (String) message.get("content");
//            }
//        }
//
//        // 메뉴 추천을 하나로 제한
//        String singleRecommendation = parseSingleRecommendation(recommandation);
//
//        return new AiChatResponseDto(singleRecommendation.trim());
//    }
//
//    private String parseSingleRecommendation(String recommendation) {
//
//        // 간단한 줄바꿈이나 마침표를 기준으로 첫 번째 메뉴만 반환
//        if (recommendation == null || recommendation.isEmpty()) {
//
//            return "추천할 메뉴가 없습니다.";
//        }
//        String[] parts = recommendation.split("[\n.,]]");
//
//        return parts.length > 0 ? parts[0] : recommendation;
//    }
//}
