package com.retryreplayframwork.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.retryreplayframwork.enums.RetryStrategy;
import com.retryreplayframwork.model.RetryJob;
import com.retryreplayframwork.repository.RetryJobRepository;
import com.retryreplayframwork.strategy.RetryStrategyContext;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RetrySchedulerService {

	private final RetryJobRepository jobRepo;
	private final Map<String, RetryStrategyContext> strategyMap;

	@Scheduled(fixedDelay = 5000)
	public void scheduleRetries() {
		List<RetryJob> pendingJobs = jobRepo.findAll().stream().filter(job -> !job.isCompleted()
				&& job.getNextRetryTime() != null && job.getNextRetryTime().isBefore(LocalDateTime.now())).toList();

		for (RetryJob job : pendingJobs) {

			RetryStrategy strategyEnum = job.getStrategy();
			if (strategyEnum == null) {
				System.out.println("Skipping job ID " + job.getId() + " — retry strategy is null");
				continue;
			}

			RetryStrategyContext strategy = strategyMap.get(strategyEnum.name());

			if (strategy == null) {
				System.out.println(
						"Skipping job ID " + job.getId() + " — no strategy implementation for: " + strategyEnum.name());
				continue;
			}

			if (strategy.shouldRetry(job)) {
				job.setCurrentAttempt(job.getCurrentAttempt() + 1);
				long interval = strategy.computeNextInterval(job);
				job.setNextRetryTime(LocalDateTime.now().plusNanos(interval * 1_000_000));

				boolean success = simulateJobExecution(job.getPayload());

				if (success) {
					job.setCompleted(true);
				}

				jobRepo.save(job);
			}
		}
	}

	private boolean simulateJobExecution(String payload) {
		return Math.random() > 0.5;
	}
}
