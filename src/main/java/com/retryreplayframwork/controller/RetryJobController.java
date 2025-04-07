package com.retryreplayframwork.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retryreplayframwork.dto.ReplayRequestDto;
import com.retryreplayframwork.dto.RetryRequestDto;
import com.retryreplayframwork.model.RetryJob;
import com.retryreplayframwork.repository.RetryJobRepository;
import com.retryreplayframwork.service.RetryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/retry")
public class RetryJobController {

	private final RetryJobRepository jobRepo;

	@GetMapping("/jobs")
	public List<RetryJob> getAllJobs() {
		return jobRepo.findAll();
	}

	@PostMapping("/create")
	public ResponseEntity<?> createJob(@RequestBody RetryRequestDto dto) {
		System.out.println(">> Incoming payload: " + dto.getPayload());
		System.out.println(">> Incoming strategy: " + dto.getStrategy());

		if (dto.getStrategy() == null) {
			return ResponseEntity.badRequest().body("Retry strategy is missing");
		}

		RetryJob job = new RetryJob();
		job.setPayload(dto.getPayload());
		job.setStrategy(dto.getStrategy());
		job.setCurrentAttempt(0);
		job.setCompleted(false);
		job.setNextRetryTime(LocalDateTime.now().plusSeconds(5));

		RetryJob saved = jobRepo.save(job);
		return ResponseEntity.ok(saved);
	}

	@Autowired
	private RetryService retryService;

	@PostMapping("/replay")
	public ResponseEntity<String> replayJob(@RequestBody ReplayRequestDto dto) {
		Optional<RetryJob> optionalJob = jobRepo.findById(dto.getJobId());

		if (optionalJob.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Retry Job not found");
		}

		RetryJob job = optionalJob.get();

		if (!job.isCompleted() && !job.isCircuitOpen()) {
			return ResponseEntity
					.ok("This job is still pending or in progress. Please wait until it completes before replaying.");
		}

		job.setCompleted(false);
		job.setCurrentAttempt(0);
		job.setNextRetryTime(LocalDateTime.now());
		job.setReplayed(true);

		job.setReplayScope(dto.getReplayScope());
		job.setTransactionType(dto.getTransactionType());
		job.setReplayScheduled(dto.isScheduleReplay());
		job.setScheduledReplayTime(dto.getScheduledTime());

		jobRepo.save(job);

		if (dto.isScheduleReplay()) {
			return ResponseEntity.ok("Replay scheduled successfully.");
		}

		boolean success = retryService.replayExecution(dto, job.getPayload());

		return success ? ResponseEntity.ok("Replay executed successfully.")
				: ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Replay failed.");
	}

	@PostMapping("/manual/{jobId}")
	public ResponseEntity<String> manuallyRetry(@PathVariable Long jobId) {
		Optional<RetryJob> optional = jobRepo.findById(jobId);

		if (optional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found");
		}

		RetryJob job = optional.get();

		if (job.isCompleted()) {
			return ResponseEntity.badRequest().body("Job already completed");
		}

		boolean success = retryService.retryExecution(job.getPayload());
		if (success) {
			job.setCompleted(true);
			jobRepo.save(job);
			return ResponseEntity.ok("Job retried and completed successfully");
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Retry failed");
		}
	}

}
