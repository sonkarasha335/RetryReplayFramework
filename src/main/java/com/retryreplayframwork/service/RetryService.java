package com.retryreplayframwork.service;

import java.time.LocalDateTime;
import java.util.List;

//package com.yourcompany.retry.service;
//
//import com.yourcompany.retry.dto.ReplayRequestDto;
//import com.yourcompany.retry.model.RetryJob;
//import com.yourcompany.retry.repository.RetryJobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.retryreplayframwork.dto.ReplayRequestDto;
import com.retryreplayframwork.model.RetryJob;
import com.retryreplayframwork.repository.RetryJobRepository;

@Service
public class RetryService {

	@Autowired
	private RetryJobRepository jobRepo;

	public boolean replayExecution(ReplayRequestDto dto, String payload) {
		if (!validateReplayContext(dto)) {
			return false;
		}

		if (dto.isStateful()) {
			return replayStateful(payload);
		} else {
			return replayStateless(payload);
		}
	}

	private boolean validateReplayContext(ReplayRequestDto dto) {
		return dto.getReplayScope() != null && dto.getTransactionType() != null;
	}

	private boolean replayStateful(String payload) {
		System.out.println("Executing stateful replay for: " + payload);
		return Math.random() > 0.2;
	}

	private boolean replayStateless(String payload) {
		System.out.println("Executing stateless replay for: " + payload);
		return Math.random() > 0.3;
	}

	@Scheduled(fixedDelayString = "${replay.scheduler.fixed-delay-ms:5000}")
	public void processScheduledReplays() {
		List<RetryJob> replays = jobRepo.findAll().stream()
				.filter(job -> job.isReplayScheduled() && job.getScheduledReplayTime() != null
						&& job.getScheduledReplayTime().isBefore(LocalDateTime.now()) && !job.isCompleted())
				.toList();

		for (RetryJob job : replays) {
			ReplayRequestDto dto = new ReplayRequestDto();
			dto.setJobId(job.getId());
			dto.setStateful(true);
			dto.setReplayScope(job.getReplayScope());
			dto.setTransactionType(job.getTransactionType());

			boolean result = replayExecution(dto, job.getPayload());

			if (result) {
				job.setCompleted(true);
				job.setReplayScheduled(false);
				jobRepo.save(job);
			}
		}
	}

	public boolean retryExecution(String payload) {
		try {
			System.out.println("Retrying job with payload: " + payload);

			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
}
