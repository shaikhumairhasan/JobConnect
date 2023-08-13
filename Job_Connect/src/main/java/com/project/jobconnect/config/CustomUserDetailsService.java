package com.project.jobconnect.config;


import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.jobconnect.entity.HR;
import com.project.jobconnect.entity.User;
import com.project.jobconnect.repository.HRRepository;
import com.project.jobconnect.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private HRRepository hrRepository;
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
				
		Optional<HR> hrOptional = hrRepository.findByEmail(email);
        Optional<User> userOptional = userRepository.findByEmail(email);
        
        if (hrOptional.isPresent()) {
            HR hr = hrOptional.get();
            // Construct HR UserDetails and return
            Set<GrantedAuthority> authorities = hr.getRoles().stream()
                    .map((role) -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toSet());

            return new org.springframework.security.core.userdetails.User(
                    email,
                    hr.getPassword(),
                    authorities
            );
        } else if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Construct User UserDetails and return
            Set<GrantedAuthority> authorities = user.getRoles().stream()
                    .map((role) -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toSet());

            return new org.springframework.security.core.userdetails.User(
                    email,
                    user.getPassword(),
                    authorities
            );
        } else {
            throw new UsernameNotFoundException("User not found.");
        }
	}

}
