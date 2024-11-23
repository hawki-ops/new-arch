package com.siemens.trail.controller;

import com.siemens.trail.dto.CreateUserRequestDTO;
import com.siemens.trail.service.UserService;
import com.siemens.trail.utilites.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Random;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequestDTO requestDTO) {
        userService.createUser(requestDTO);
        return ResponseEntity.ok().body(Collections.singletonMap("message", "User added successfully!"));
    }

    @GetMapping("/error")
    public ResponseEntity<APIResponse<String>> error() {
        Random random = new Random();
        double randomDouble = random.nextDouble();

        if (randomDouble < 0.5) {
            return ResponseEntity.ok().body(new APIResponse<>("Request Failed", 400));
        } else {
            return ResponseEntity.ok().body(new APIResponse<>("The request was successful!"));
        }
    }


}
