package com.retryreplayframwork.model;

import java.time.LocalDateTime;

import com.retryreplayframwork.enums.RetryStrategy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RetryJob {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String payload;

	@Enumerated(EnumType.STRING)
	@Column(length = 50)
	private RetryStrategy strategy;

	private int maxAttempts;

	private int currentAttempt;

	private long intervalMillis;

	private LocalDateTime nextRetryTime;

	private boolean completed;

	private boolean circuitOpen;

	private boolean replayed;
	private boolean replayScheduled;
	private LocalDateTime scheduledReplayTime;
	private String replayScope;
	private String transactionType;
}
