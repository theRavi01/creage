package com.creage.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.creage.service.UsersService;
import com.creage.dto.UserRegistrationDto;
import com.creage.dto.OtpVerificationDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UsersController {
	
	 @Autowired
	 private UsersService userService;
	 
	private static final Logger LOG = LoggerFactory.getLogger(UsersController.class);
	
	@GetMapping("/test")
	public String test() {
		LOG.info("Creage is Live now...{}");
		return "Creage is Live now....";
	}

	

	    @PostMapping("/register")
	    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegistrationDto dto) {
			LOG.info("register email : {}",dto.getEmail());
	        return ResponseEntity.ok(userService.registerUser(dto));
	    }

	    @PostMapping("/verify-otp")
	    public ResponseEntity<String> verifyOtp(@Valid @RequestBody OtpVerificationDto dto) {
	        return ResponseEntity.ok(userService.verifyOtpAndCreateUser(dto));
	    }
	    
}
