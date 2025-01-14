//package com.example.tastefulai.domain.aichat.controller;
//
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/users/aiChats")
//public class AiChatController {
//
//    private final OpenAiChatModel openAiChatModel;
//    private final VertexAiGeminiChatModel vertexAiGeminiChatModel;
//
//    public ChatController(OpenAiChatModel openAiChatModel, VertexAiGeminiChatModel vertexAiGeminiChatModel) {
//        this.openAiChatModel = openAiChatModel;
//        this.vertexAiGeminiChatModel = vertexAiGeminiChatModel;
//    }
//
//    @GetMapping("/chat")
//    public Map<String, String> chat(@RequestBody String message) {
//        Map<String, String> responses = new HashMap<>();
//
//        String openAiResponse = openAiChatModel.call(message);
//        responses.put("openai(chatGPT) 응답", openAiResponse);
//
//        String vertexAiGeminiResponse = vertexAiGeminiChatModel.call(message);
//        responses.put("vertexai(gemini) 응답", vertexAiGeminiResponse);
//        return responses;
//    }
//}