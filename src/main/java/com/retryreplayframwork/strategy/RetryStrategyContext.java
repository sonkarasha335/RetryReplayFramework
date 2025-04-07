package com.retryreplayframwork.strategy;

import com.retryreplayframwork.model.RetryJob;

public interface RetryStrategyContext {
	long computeNextInterval(RetryJob job);

	boolean shouldRetry(RetryJob job);
}