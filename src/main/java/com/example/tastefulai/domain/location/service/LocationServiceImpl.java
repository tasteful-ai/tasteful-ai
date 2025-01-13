package com.example.tastefulai.domain.location.service;

import com.example.tastefulai.domain.location.dto.LocationResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationServiceImpl implements LocationService {

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    /**
     * 키워드로 장소를 검색하는 메서드
     * @param keyword 검색할 키워드
     * @return LocationResponseDto 리스트 (검색 결과)
     */
    @Override
    public List<LocationResponseDto> findByKeyword(String keyword) {
        // Kakao API의 키워드 검색 URL
        String url = "https://dapi.kakao.com/v2/local/search/keyword.json";

        String requestUrl = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("query", keyword)   // 검색 키워드
                .queryParam("radius", 5000) // 5km 반경
                .queryParam("size", 10) // 최대 10개 결과
                .toUriString();

        // HTTP 요청 헤더 설정: Kakao API 키 포함
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<Map> response = restTemplate.exchange(requestUrl, HttpMethod.GET, entity, Map.class);
            log.info("카카오 API 응답: {}", response.getBody());
            return parseResponse(response.getBody());
        } catch (HttpClientErrorException e) {
            log.error("API 호출 실패: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Kakao API 응답 데이터를 파싱하여 LocationResponseDto 리스트로 변환
     * @param response Kakao API 응답 데이터
     * @return LocationResponseDto 리스트
     */
    private List<LocationResponseDto> parseResponse(Map<String, Object> response) {
        List<LocationResponseDto> results = new ArrayList<>();
        if (response.containsKey("documents")) {
            List<Map<String, Object>> places = (List<Map<String, Object>>) response.get("documents");
            for (Map<String, Object> place : places) {
                results.add(LocationResponseDto.builder()
                        .placeName((String) place.get("place_name"))
                        .address((String) place.get("address_name"))
                        .phone((String) place.get("phone"))
                        .latitude(Double.parseDouble((String) place.get("y")))
                        .longitude(Double.parseDouble((String) place.get("x")))
                        .build());
            }
        }
        return results;
    }
}