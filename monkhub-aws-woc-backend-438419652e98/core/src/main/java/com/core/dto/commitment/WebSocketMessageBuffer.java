package com.core.dto.commitment;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WebSocketMessageBuffer {

    private static final Queue<CommitmentResponseDto> messageQueues = new ConcurrentLinkedQueue<>();

    public static void setMessageQueues(CommitmentResponseDto commitmentResponseDto) {
        messageQueues.add(commitmentResponseDto);
    }

    public static CommitmentResponseDto getMessageQueues() {
        return messageQueues.poll();
    }

    public static void clearMessageQueues() {
        messageQueues.clear();
    }
}
