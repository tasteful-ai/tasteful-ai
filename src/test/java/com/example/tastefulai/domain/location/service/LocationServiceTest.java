package com.example.tastefulai.domain.location.service;

import com.example.tastefulai.domain.location.dto.LocationResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class LocationServiceTest {

    @Autowired
    private LocationService locationService;

    @Test
    public void testFindByKeyword() {
        String keyword = "고기";
        List<LocationResponseDto> results = locationService.findByKeyword(keyword);

        assertFalse(results.isEmpty()); // 결과가 비어 있지 않아야 함
        assertEquals("고기집", results.get(0).getPlaceName()); // 예상 결과와 일치하는지 확인
    }
}