package com.retryreplayframwork.strategy;

import org.springframework.stereotype.Component;

import com.retryreplayframwork.model.RetryJob;

@Component("EXPONENTIAL_BACKOFF")
public class ExponentialBackoffStrategy implements RetryStrategyContext {
	@Override
	public long computeNextInterval(RetryJob job) {
		return job.getIntervalMillis() * (long) Math.pow(2, job.getCurrentAttempt());
	}

	@Override
	public boolean shouldRetry(RetryJob job) {
		return job.getCurrentAttempt() < job.getMaxAttempts();
	}
}