package com.project.jobconnect.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.jobconnect.entity.Job;
import com.project.jobconnect.entity.User;
import com.project.jobconnect.repository.JobRepository;
import com.project.jobconnect.repository.UserRepository;

@Service
@Transactional
public class JobService {

    private final JobRepository jobDAO;
    private final UserRepository userDAO;

    @Autowired
    public JobService(JobRepository jobDAO, UserRepository userDAO) {
        this.jobDAO = jobDAO;
		this.userDAO = userDAO;
    }

    public List<Job> getAllJobs() {
        return jobDAO.findAll();
    }

    public Optional<Job> getJobById(Long id) {
    	return jobDAO.findById(id)
    	        .map(job -> {
    	            // Eagerly fetch the users collection
    	            job.getUsers().size();
    	            return job;
    	        });
    }

    public Job saveJob(Job job) {
        return jobDAO.save(job);
    }

    public void deleteJob(Job job) {
        jobDAO.delete(job);
    }

    public void deleteJobById(Long id) {
        jobDAO.deleteById(id);
    }
}
