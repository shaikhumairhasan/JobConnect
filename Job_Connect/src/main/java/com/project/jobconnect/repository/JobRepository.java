package com.project.jobconnect.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.jobconnect.entity.Job;
import com.project.jobconnect.entity.User;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
}
