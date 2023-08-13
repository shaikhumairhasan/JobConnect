package com.project.jobconnect.controller;

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
import org.springframework.web.servlet.ModelAndView;

import com.project.jobconnect.entity.HR;
import com.project.jobconnect.entity.Job;
import com.project.jobconnect.entity.User;
import com.project.jobconnect.repository.UserRepository;
import com.project.jobconnect.service.HRService;
import com.project.jobconnect.service.JobService;
import com.project.jobconnect.service.RoleService;
import com.project.jobconnect.service.UserService;

@Controller
public class UserController {

	private final HRService hrService;
	private final UserService userService;
	private final JobService jobService;
	private final RoleService roleService;
	private final UserRepository userRepository;

	@Autowired
	public UserController(HRService hrService, UserService userService, JobService jobService,
			RoleService roleService, UserRepository userRepository) {
		this.hrService = hrService;
		this.userService = userService;
		this.jobService = jobService;
		this.roleService = roleService;
		this.userRepository = userRepository;
	}

	@GetMapping("/viewalljobs")
	public ModelAndView viewAllJobs() {
		ModelAndView modelAndView = new ModelAndView("viewalljobs");
		modelAndView.addObject("jobs", jobService.getAllJobs());
		return modelAndView;
	}

	@GetMapping("/apply/{jobId}")
	public String applyJob(@PathVariable(name = "jobId") Long jobId) {
        Job job = jobService.getJobById(jobId).orElse(null);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	    String email = userDetails.getUsername();
	    
	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new UsernameNotFoundException("User not found by email"));

	    user.getJobs().add(job);
	    userService.saveUser(user);
	    	    
		return "redirect:/viewalljobs?jobIdParam="+job.getJobId();
	}
}