package com.retryreplayframwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.retryreplayframwork.model.RetryJob;

@Repository
public interface RetryJobRepository extends JpaRepository<RetryJob, Long> {
}
