package com.project.jobconnect.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.project.jobconnect.entity.HR;
import com.project.jobconnect.entity.User;
import com.project.jobconnect.service.HRService;
import com.project.jobconnect.service.JobService;
import com.project.jobconnect.service.RoleService;
import com.project.jobconnect.service.UserService;

@Controller
public class OperationController {

	private final HRService hrService;
	private final UserService userService;
	private final JobService jobService;
	private final RoleService roleService;

	@Autowired
	public OperationController(HRService hrService, UserService userService, JobService jobService,
			RoleService roleService) {
		this.hrService = hrService;
		this.userService = userService;
		this.jobService = jobService;
		this.roleService = roleService;
	}

	@PostMapping("/hrregister")
	public String hrRegister(@ModelAttribute HR hr) {
		hrService.saveHR(hr);
		return "redirect:/login";
	}

	@PostMapping("/userregister")
	public String registerUser(@RequestParam("username") String username, @RequestParam("email") String email,
			@RequestParam("password") String password, @RequestParam("resume") MultipartFile resume) {
		try {
			User user = new User();
			user.setUsername(username);
			user.setEmail(email);
			user.setPassword(password);
			user.setResume(resume.getBytes());

			userService.saveUser(user);

			return "redirect:/login";
		} catch (Exception e) {
			// Handle the exception
			return "redirect:/error";
		}
	}

	@GetMapping("/hroruser")
	public String hrOrUser() {

		Collection<? extends GrantedAuthority> role;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String roleName = "";
		if (authentication != null) {
			role = authentication.getAuthorities();
			roleName = role.toString();
		}

		if (roleName.contains("HR"))
			return "redirect:/hrpanel";
		else
			return "redirect:/viewalljobs";
	}
}
