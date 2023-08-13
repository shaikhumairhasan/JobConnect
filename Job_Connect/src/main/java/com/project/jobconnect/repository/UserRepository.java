package com.project.jobconnect.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.jobconnect.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
	Optional<User> findByEmail(String email);
	Set<User> findByJobsJobId(Long jobId);

}
