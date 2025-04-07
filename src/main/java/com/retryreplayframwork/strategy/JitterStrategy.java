package com.retryreplayframwork.strategy;

import java.util.Random;

import org.springframework.stereotype.Component;

import com.retryreplayframwork.model.RetryJob;

@Component("JITTER")
public class JitterStrategy implements RetryStrategyContext {
	private final Random random = new Random();

	@Override
	public long computeNextInterval(RetryJob job) {
		long base = job.getIntervalMillis();
		long jitter = (long) (base * 0.5 * random.nextDouble());
		return base + jitter;
	}

	@Override
	public boolean shouldRetry(RetryJob job) {
		return job.getCurrentAttempt() < job.getMaxAttempts();
	}
}
