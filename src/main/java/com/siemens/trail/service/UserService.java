package com.siemens.trail.service;

import com.siemens.trail.dto.CreateUserRequestDTO;
import com.siemens.trail.model.WebUser;
import com.siemens.trail.repository.WebUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private WebUserRepository webUserRepository;

    public void createUser(CreateUserRequestDTO requestDTO) {
        // Check for duplicate username or email
        if (webUserRepository.existsByUsername(requestDTO.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // Create and save new user
        WebUser user = new WebUser();
        user.setUsername(requestDTO.getUsername());
        user.setPassword(requestDTO.getPassword()); // Hash password if needed

        webUserRepository.save(user);
    }
}
