package com.creage.serviceImpl;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.creage.controller.UsersController;
import com.creage.dto.OtpVerificationDto;
import com.creage.dto.UserRegistrationDto;
import com.creage.exception.ValidationException;
import com.creage.model.Otp;
import com.creage.model.PlanType;
import com.creage.model.Subscription;
import com.creage.model.Users;
import com.creage.repository.OtpRepository;
import com.creage.repository.UsersRepository;
import com.creage.service.SubscriptionService;
import com.creage.service.UsersService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UsersServiceImpl implements UsersService {

	private static final Logger LOG = LoggerFactory.getLogger(UsersController.class);

	private final UsersRepository usersRepository;
	private final OtpRepository otpRepository;
	private final EmailService emailService;
	private final SubscriptionService subService;
	private final Random random = new Random();

	public UsersServiceImpl(UsersRepository usersRepository, OtpRepository otpRepository, EmailService emailService
			,SubscriptionService subService) {
		this.usersRepository = usersRepository;
		this.otpRepository = otpRepository;
		this.emailService = emailService;
		this.subService = subService;
	}

	@Override
	public String registerUser(UserRegistrationDto dto) {
		LOG.info("register email : {}", dto.getEmail());
		if (usersRepository.existsByEmail(dto.getEmail())) {
			log.info("Email already registered: {}", dto.getEmail());
			throw new ValidationException("Email already registered");
		}

		// Generate OTP
		String otpCode = String.format("%06d", random.nextInt(1000000));

		// Save OTP in DB
		Otp otp = new Otp();
		otp.setEmail(dto.getEmail());
		otp.setOtp(otpCode);
		if(otpRepository.existsByEmail(dto.getEmail())) {
		otpRepository.deleteByEmail(dto.getEmail());
		}
		Otp createdOtp = otpRepository.save(otp);
		emailService.sendEmail(createdOtp.getEmail(), otpCode);

		return "OTP has been sent to your email.";
	}

	@Override
	@Transactional
	public String verifyOtpAndCreateUser(OtpVerificationDto dto) {
		Optional<Otp> otpRecord = otpRepository.findByEmailAndOtp(dto.getEmail(), dto.getOtp());

		if (otpRecord.isEmpty()) {
			throw new ValidationException("Invalid OTP");
		}

		if (usersRepository.existsByEmail(dto.getEmail())) {
			throw new ValidationException("User already verified");
		}

		// Create user
		Users newUser = new Users();
		newUser.setEmail(dto.getEmail());
		newUser.setPassword(dto.getPassword());
		newUser.setRole(dto.getRole());
		Users savedUser = usersRepository.save(newUser);
		Subscription subscription = new Subscription();
        subscription.setUser(savedUser);
        subscription.setSubscriptionStartDate(LocalDate.now());
        subscription.setSubscriptionEndDate(LocalDate.now().plusMonths(1));
        subscription.setValid(true);
        subscription.setPlanType(PlanType.FREE);
		subService.subscribeUser(savedUser.getId(), subscription); 
		// Delete OTP record after verification
		otpRepository.deleteByEmail(dto.getEmail()); 

		return "User successfully registered.";
	}
}
