package com.example.tastefulai.domain.location.controller;

import com.example.tastefulai.domain.location.dto.LocationResponseDto;
import com.example.tastefulai.domain.location.service.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/location")
@RequiredArgsConstructor
@Slf4j
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    public String searchLocationPage(@RequestParam(required = false) String keyword, Model model) {
        if (keyword != null) {
            List<LocationResponseDto> results = locationService.findByKeyword(keyword);
            model.addAttribute("results", results);
            log.info("HTML 요청으로 검색 결과 리스트 크기: {}", results.size());
        }
        model.addAttribute("title", "Location Search");

        return "location"; // location.html 템플릿
    }
}