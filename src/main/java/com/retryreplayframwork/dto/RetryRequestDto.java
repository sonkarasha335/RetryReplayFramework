package com.retryreplayframwork.dto;

import com.retryreplayframwork.enums.RetryStrategy;

import lombok.Data;

@Data
public class RetryRequestDto {
	private String payload;
	private RetryStrategy strategy;
	private long intervalMillis;
	private int maxAttempts;
}
