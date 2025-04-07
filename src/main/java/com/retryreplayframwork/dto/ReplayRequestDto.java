package com.retryreplayframwork.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReplayRequestDto {
	private Long jobId;
	private boolean stateful;
	private boolean scheduleReplay;
	private LocalDateTime scheduledTime;
	private String replayScope;
	private String transactionType;
}
