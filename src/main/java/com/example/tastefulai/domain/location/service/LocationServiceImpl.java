package com.example.tastefulai.domain.location.service;

import com.example.tastefulai.domain.location.dto.LocationResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationServiceImpl implements LocationService {

    @Qualifier("locationRedisTemplate")
    private final RedisTemplate<String, Object> locationRedisTemplate;

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    @Override
    public List<LocationResponseDto> findByKeyword(String keyword) {
        String cacheKey = "search:" + keyword;

        List<LocationResponseDto> cachedResults = (List<LocationResponseDto>) locationRedisTemplate.opsForValue().get(cacheKey);
        if (cachedResults != null) {
            log.info("Redis 캐시에서 검색 결과를 가져옴: {}", cacheKey);
            return cachedResults;
        }

        List<LocationResponseDto> results = fetchFromKakaoApi(keyword);

        if (!results.isEmpty()) {
            locationRedisTemplate.opsForValue().set(cacheKey, results, Duration.ofMinutes(5));  // 캐시 TTL 설정 (5분)
            log.info("Redis 캐시에 검색 결과 저장: {}", cacheKey);
        }

        return results;
    }

    private List<LocationResponseDto> fetchFromKakaoApi(String keyword) {
        String url = "https://dapi.kakao.com/v2/local/search/keyword.json";
        String requestUrl = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("query", keyword)
                .queryParam("radius", 5000)
                .queryParam("size", 10)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<Map> response = restTemplate.exchange(requestUrl, HttpMethod.GET, entity, Map.class);
            log.info("카카오 API 응답: {}", response.getBody());
            return parseResponse(response.getBody());
        } catch (HttpClientErrorException exception) {
            log.error("API 호출 실패: {}", exception.getMessage());
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