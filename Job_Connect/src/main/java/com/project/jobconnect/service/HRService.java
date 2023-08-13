package com.project.jobconnect.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.jobconnect.entity.HR;
import com.project.jobconnect.entity.Role;
import com.project.jobconnect.repository.HRRepository;
import com.project.jobconnect.repository.RoleRepository;

@Service
public class HRService {
	
    private final HRRepository hrDAO;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public HRService(HRRepository hrDAO, RoleRepository roleRepository) {
        this.hrDAO = hrDAO;
		this.roleRepository = roleRepository;
		this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public List<HR> getAllHRs() {
        return hrDAO.findAll();
    }

    public Optional<HR> getHRById(Long id) {
        return hrDAO.findById(id);
    }

    public HR saveHR(HR hr) {
    	String encodedPassword = passwordEncoder.encode(hr.getPassword());
    	hr.setPassword(encodedPassword);
    	
    	Role hrRole = roleRepository.findByName("HR");
    	
    	if(hrRole == null){
    		hrRole = new Role();
    		hrRole.setName("HR");
        }
    	
    	Set<Role> roles = new HashSet<>();
    	roles.add(hrRole);
    	
    	hr.setRoles(roles);
        return hrDAO.save(hr);
    }

    public void deleteHR(HR hr) {
        hrDAO.delete(hr);
    }
    
    public void deleteHRById(Long id) {
        hrDAO.deleteById(id);
    }
}
