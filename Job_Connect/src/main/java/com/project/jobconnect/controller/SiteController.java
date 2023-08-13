package com.project.jobconnect.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SiteController {

	@GetMapping("/")
	public String home() {
		return "home";
	}
	
	@GetMapping("/home")
	public String backHome() {
		return "home";
	}
	
	@GetMapping("/about")
	public String about() {
		return "about";
	}
	
	@GetMapping("/login")
	public String hrLogin() {
		return "login";
	}
	
	@GetMapping("/registration")
	public String Registration() {
		return "registration";
	}

}
