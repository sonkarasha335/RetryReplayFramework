package com.retryreplayframwork.strategy;

import org.springframework.stereotype.Component;

import com.retryreplayframwork.model.RetryJob;

@Component("FIXED")
public class FixedIntervalStrategy implements RetryStrategyContext {
	@Override
	public long computeNextInterval(RetryJob job) {
		return job.getIntervalMillis();
	}

	@Override
	public boolean shouldRetry(RetryJob job) {
		return job.getCurrentAttempt() < job.getMaxAttempts();
	}
}