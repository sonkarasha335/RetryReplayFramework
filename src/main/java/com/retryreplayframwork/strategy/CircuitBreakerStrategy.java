package com.retryreplayframwork.strategy;

import org.springframework.stereotype.Component;

import com.retryreplayframwork.model.RetryJob;

@Component("CIRCUIT_BREAKER")
public class CircuitBreakerStrategy implements RetryStrategyContext {
	@Override
	public long computeNextInterval(RetryJob job) {
		return job.getIntervalMillis();
	}

	@Override
	public boolean shouldRetry(RetryJob job) {
		if (job.isCircuitOpen()) {
			return false;
		}
		if (job.getCurrentAttempt() >= job.getMaxAttempts()) {
			job.setCircuitOpen(true);
			return false;
		}
		return true;
	}
}