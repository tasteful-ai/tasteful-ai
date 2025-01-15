package com.example.tastefulai.domain.chatting;

import com.example.tastefulai.domain.chatting.repository.ChattingMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageCleanupScheduler {

    private final ChattingMessageRepository chattingMessageRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteOldMessages() {
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);

        int deletedCount = chattingMessageRepository.deleteByCreatedAtBefore(oneDayAgo);

        log.info("Deleted {} old messages older than {}", deletedCount, oneDayAgo);
    }
}
