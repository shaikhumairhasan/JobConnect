package com.project.jobconnect.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.jobconnect.entity.HR;

@Repository
public interface HRRepository extends JpaRepository<HR, Long> {

	Optional<HR> findByEmail(String email);
	
	List<HR> findByHRId(Long hRId);
	
}