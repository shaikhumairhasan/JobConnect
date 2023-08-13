package com.project.jobconnect.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.jobconnect.entity.Role;
import com.project.jobconnect.entity.User;
import com.project.jobconnect.repository.RoleRepository;
import com.project.jobconnect.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userDAO;
	private final RoleRepository roleRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	@Autowired
	public UserService(UserRepository userDAO, RoleRepository roleRepository) {
		this.userDAO = userDAO;
		this.roleRepository = roleRepository;
		this.passwordEncoder = new BCryptPasswordEncoder();
	}

	public List<User> getAllUsers() {
		return userDAO.findAll();
	}

	public Optional<User> getUserById(Long id) {
		return userDAO.findById(id);
	}

	public User saveUser(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);

		Role userRole = roleRepository.findByName("USER");

		if (userRole == null) {
			userRole = new Role();
			userRole.setName("USER");
		}

		Set<Role> roles = new HashSet<>();
		roles.add(userRole);

		user.setRoles(roles);

		return userDAO.save(user);
	}

	public void deleteUser(User user) {
		userDAO.delete(user);
	}

	public void deleteUserById(Long id) {
		userDAO.deleteById(id);
	}
}
