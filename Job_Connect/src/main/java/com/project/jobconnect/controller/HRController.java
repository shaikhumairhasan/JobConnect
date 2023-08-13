package com.project.jobconnect.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.project.jobconnect.entity.HR;
import com.project.jobconnect.entity.Job;
import com.project.jobconnect.entity.User;
import com.project.jobconnect.repository.HRRepository;
import com.project.jobconnect.repository.JobRepository;
import com.project.jobconnect.repository.UserRepository;
import com.project.jobconnect.service.HRService;
import com.project.jobconnect.service.JobService;
import com.project.jobconnect.service.UserService;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class HRController {
	
	private final HRService hrService;
	private final JobService jobService;
	private final UserService userService;
	private final UserRepository userRepository;
	private final JobRepository jobRepository;
	private final HRRepository hrRepository;
	
	@Autowired
	public HRController(HRService hrService, JobService jobService, HRRepository hrRepository, UserService userService, UserRepository userRepository, JobRepository jobRepository) {
		this.hrService = hrService;
		this.jobService = jobService;
		this.userService = userService;
		this.userRepository = userRepository;
		this.jobRepository = jobRepository;
		this.hrRepository = hrRepository;
	}

	@GetMapping("/hrpanel")
	public String hrPanel() {
		return "hrpanel";
	}
	
	@GetMapping("/postnewjob")
	public String postNewJob() {
		return "postnewjob";
	}
	
	@GetMapping("/allview")
	public ModelAndView allview() {
		HR hr;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	    String email = userDetails.getUsername();
	    hr = hrRepository.findByEmail(email)
	            .orElseThrow(() -> new UsernameNotFoundException("HR not found by email"));

		ModelAndView modelAndView = new ModelAndView("allview");
		modelAndView.addObject("jobs", hr.getJobs());
		return modelAndView;
	}
	
	@PostMapping("/newjob")
	public String newJob(@ModelAttribute Job job) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	    String email = userDetails.getUsername();
	    
	    HR hr = hrRepository.findByEmail(email)
	            .orElseThrow(() -> new UsernameNotFoundException("HR not found by email"));

	        hr.getJobs().add(job);
	        job.setHr(hr);

	        jobService.saveJob(job);
		return "redirect:/hrpanel";
	}
	
	@PostMapping("/editjob")
	public String editJob(@ModelAttribute Job editedJob) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	    String email = userDetails.getUsername();
	    
	    HR hr = hrRepository.findByEmail(email)
	            .orElseThrow(() -> new UsernameNotFoundException("HR not found by email"));

	    Job existingJob = jobService.getJobById(editedJob.getJobId())
	            .orElseThrow(() -> new IllegalArgumentException("Job not found"));
	    
	    existingJob.setJobTitle(editedJob.getJobTitle());
	    existingJob.setJobDescription(editedJob.getJobDescription());
	    existingJob.setCompanyName(editedJob.getCompanyName());
	    existingJob.setCIN(editedJob.getCIN());
	    existingJob.setSalary(editedJob.getSalary());
	    existingJob.setLocation(editedJob.getLocation());

	    jobService.saveJob(existingJob);
	    return "redirect:/hrpanel";
	}

	
	@GetMapping("/{jobId}")
	public ModelAndView editOwner(@PathVariable(name = "jobId") Long jobId) {
		Job job = jobService.getJobById(jobId).orElse(new Job());
		ModelAndView modelAndView = new ModelAndView("editexistingjob");
		modelAndView.addObject("job", job);
		return modelAndView;
	}
	
	@GetMapping("/delete/{jobId}")
	public String deleteJob(@PathVariable(name = "jobId") Long jobId) {
		jobService.deleteJobById(jobId);
		return "redirect:/allview";
	}
	
	@GetMapping("/applicants={jobId}")
	public ModelAndView applicants(@PathVariable(name = "jobId") Long jobId) {
		ModelAndView modelAndView = new ModelAndView("applicants");
	    Set<User> users = userRepository.findByJobsJobId(jobId);
//	    Map<Long, String> bases = new HashMap<>();
//	    System.out.println(users);
//	    users.forEach(user -> {
//	        byte[] resumeBytes = user.getResume();
//	        String base64Resume = Base64.getEncoder().encodeToString(resumeBytes);
//	        bases.put(user.getUserID(), base64Resume);
//	    });
	    modelAndView.addObject("users", users);
//	    modelAndView.addObject("bases", bases);

	    return modelAndView;
	}
	
	
	@GetMapping("/download-pdf")
	public void downloadPDF(@RequestParam("id") Long id, HttpServletResponse response) throws IOException {
	    Optional<User> temp = userService.getUserById(id);

	    if (temp.isPresent()) {
	        User user = temp.get();

	        response.setContentType("application/pdf");
	        String headerKey = "Content-Disposition";
	        String headerValue = "attachment; filename=\"" + user.getUsername() + "_resume.pdf\"";
	        response.setHeader(headerKey, headerValue);
	        ServletOutputStream outputStream = response.getOutputStream();
	        outputStream.write(user.getResume());
	        outputStream.close();
	    }
	}

}
