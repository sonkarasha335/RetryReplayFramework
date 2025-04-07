package com.retryreplayframwork.enums;

public enum RetryStrategy {
	EXPONENTIAL_BACKOFF, CIRCUIT_BREAKER, JITTER, FIXED_INTERVAL
}