package com.core.scheduler;

import com.core.dto.commitment.CommitmentResponseDto;
import com.core.dto.commitment.WebSocketMessageBuffer;
import com.core.utility.MethodLoggerUtility;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@AllArgsConstructor
public class CommitmentScheduler {

    private final SimpMessagingTemplate messagingTemplate;

    @Scheduled(fixedDelay = 1000)
    public void sendMessage() {
        CommitmentResponseDto commitmentResponseDto = WebSocketMessageBuffer.getMessageQueues();

        if (Objects.nonNull(commitmentResponseDto)) {
            MethodLoggerUtility.start(this);

            messagingTemplate.convertAndSend("/topic/commitments", commitmentResponseDto);

            MethodLoggerUtility.end(this);
        }
    }
}
