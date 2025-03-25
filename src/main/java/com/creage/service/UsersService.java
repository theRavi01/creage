package com.creage.service;

import com.creage.dto.OtpVerificationDto;
import com.creage.dto.UserRegistrationDto;

public interface UsersService {

	public String registerUser(UserRegistrationDto dto);
	
	public String verifyOtpAndCreateUser(OtpVerificationDto dto);
}
