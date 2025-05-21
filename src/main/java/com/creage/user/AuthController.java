package com.creage.user;

import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.creage.SecurityConfig.JwtProvider;
import com.creage.dto.UserRegistrationDto;
import com.creage.email.EmailService;
import com.creage.model.Role;
import com.creage.model.Subscription;
import com.creage.model.Users;
import com.creage.request.PasswordResetRequest;
import com.creage.request.UserCredentials;
import com.creage.response.UIResponse;
import com.creage.subscription.SubscriptionService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
/**
 * This class is responsible for handling user authentication.
 *
 * @author Ravikant Pandey
 * @version 1.0
 * @since 2025
 */

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/api/v1/auth")
public class AuthController {
	
	private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private SubscriptionService subscriptionService;
	
	@Autowired
	private UsersServiceImpl customUserService;
	
	@Autowired
	private UsersRepository userRepository;
	
	private final EmailService emailService;
	
	
    
	public AuthController(EmailService emailService) {
    this.emailService = emailService;
	}
	
	
	
	 @PostMapping("/register")
	    public ResponseEntity<?> createOrUpdateUserHandler(@RequestBody UserRegistrationDto dto) throws Exception {


	        UIResponse uiResponse = new UIResponse();
	        String randomPassword = null;

	        if (dto.getEmail() == null || dto.getEmail().isEmpty()) {
	            return ResponseEntity.badRequest().body("Email is required");
	        }

	        Optional<Users> existingUserByEmail = userRepository.findByEmail(dto.getEmail());
	        Optional<Users> existingUserByUsername = userRepository.findByUsername(dto.getUserName());

	        boolean isNewUser = (existingUserByEmail.get() == null);
	        Users userToSave = isNewUser ? new Users() : existingUserByEmail.get();

	        if (existingUserByUsername != null && !existingUserByUsername.get().getEmail().equals(dto.getEmail())) {
	            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already taken by another user");
	        }

	        if (isNewUser) {
	            userToSave.setEmail(dto.getEmail());
	            userToSave.setCreateddate(LocalDateTime.now());
	            randomPassword = String.format("%08d", new Random().nextInt(100_000_000));
	            userToSave.setUsername(dto.getUserName());
	            userToSave.setPassword(passwordEncoder.encode(randomPassword));
	        } else {
	            if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
	                userToSave.setPassword(passwordEncoder.encode(dto.getPassword()));
	            }
	        }

	        // Map remaining fields
	        if (dto.getUserName() != null) userToSave.setUsername(dto.getUserName());
	        userToSave.setRole(1);
	        userToSave.setWriteaccess(1);

            
	        Users saved = userRepository.save(userToSave);
	        if(isNewUser) {
	        	Subscription subscription = new Subscription();
	            subscription.setUser(saved);
	            subscription.setSubscriptionStartDate(LocalDate.now());
	            subscription.setSubscriptionEndDate(LocalDate.now().plusMonths(1));
	            subscription.setIsValid(1);
	            subscription.setPlanType("FREE");
	            subscriptionService.subscribeUser(saved.getId(), subscription); 
	        }
	        String roleName = Role.getRoleById(saved.getRole());

	        if (isNewUser) {
	            emailService.sendWelcomeEmail(saved.getEmail(), saved.getUsername(), randomPassword);
	        }

	        Authentication authentication = new UsernamePasswordAuthenticationToken(
	                saved.getEmail(), null, AuthorityUtils.createAuthorityList(roleName));
	        ((UsernamePasswordAuthenticationToken) authentication).setDetails(saved.getWriteaccess());
	        SecurityContextHolder.getContext().setAuthentication(authentication);

	        String jwt = JwtProvider.generateToken(authentication);

	        uiResponse.setResult(true);
	        uiResponse.setApiKey(jwt);
	        uiResponse.setMessage(isNewUser ? "User created successfully" : "User updated successfully");

	        return new ResponseEntity<>(uiResponse, HttpStatus.OK);
	    }

	@PostMapping("/login")
	public ResponseEntity<?> sigin(@Valid @RequestBody UserCredentials req, HttpServletRequest request) 
	    throws UnknownHostException {

	    UIResponse uiResponse = new UIResponse();
	    String token = null;

	    // Extract user IP address
	    String ipAddress = getRequestIP(request);
	    LOG.info("User Login Attempt from IP: {}", ipAddress);

	    // Validate input
	    if ( req.getEmail()==null|| req.getPassword()==null || req.getEmail().equals("")|| req.getPassword().equals("")) {
	        uiResponse.setResult(false);
	        uiResponse.setData(null);
	        uiResponse.setApiKey(null);
	        uiResponse.setMessage("Please provide valid inputs");
	        return new ResponseEntity<>(uiResponse, HttpStatus.BAD_REQUEST);
	    }

	    try {
	        Users Users = findUserByEmailOrUsername(req.getEmail());
		    // Verify old password
//		    if (!passwordEncoder.matches(req.getPassword(), Users.getUserpassword())) {
//		    	uiResponse.setResult(false);
//		    	uiResponse.setMessage("Old password is incorrect");
//		        return ResponseEntity.ok().body(uiResponse);
//		    }
	        if (Users == null || !passwordEncoder.matches(req.getPassword(), Users.getPassword())) {
	            uiResponse.setResult(false);
	            uiResponse.setMessage("Invalid username or password");
	            return new ResponseEntity<>(uiResponse, HttpStatus.UNAUTHORIZED);
	        }

	        // Authenticate user
	        try {
	    	    // 🧠 Create authentication and attach roleid to details
	    	    Authentication authentication = new UsernamePasswordAuthenticationToken(
	    	    		Users.getEmail(),
	    	            null,
	    	            AuthorityUtils.createAuthorityList("WRITE_ACCESS") // or your actual roles
	    	    );
	    	    ((UsernamePasswordAuthenticationToken) authentication).setDetails(Users.getWriteaccess());

	    	    SecurityContextHolder.getContext().setAuthentication(authentication);

	    	    token = JwtProvider.generateToken(authentication);

	            // Hide password before sending response
	            Users.setPassword(null);
	            uiResponse.setResult(true);
	            uiResponse.setData(Users);
	            uiResponse.setApiKey(token);
	            uiResponse.setMessage("Login Success");

//	            emailService.sendLoginAlertEmail(Users.getEmail(),Users.getUsername(),ipAddress,LocalDateTime.now());
	            return new ResponseEntity<>(uiResponse, HttpStatus.OK);

	        } catch (BadCredentialsException e) {
	            // Handle incorrect password scenario
	            uiResponse.setResult(false);
	            uiResponse.setMessage("Invalid username or password");
	            return new ResponseEntity<>(uiResponse, HttpStatus.UNAUTHORIZED);
	        }

	    } catch (Exception e) {
	        LOG.error("Login API error: {}", e.getMessage());
	        uiResponse.setResult(false);
	        uiResponse.setMessage("An unexpected error occurred. Please try again.");
	        return new ResponseEntity<>(uiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	

//	******************* Forget Password Send Mail ************************************
	
	@PostMapping("/forget-password")
	public ResponseEntity<?> requestPasswordReset(@RequestBody PasswordResetRequest request) {
	    // Validate email
	    if (request.getEmail() == null || request.getEmail().isEmpty()) {
	        return ResponseEntity.badRequest().body("Email is required");
	    }

	    Optional<Users> user = userRepository.findByEmail(request.getEmail());
	    if (user == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
	    }

	    // Generate a token for the password reset
	    String resetToken = JwtProvider.generatePasswordResetToken(user.get().getEmail());

	    // Send the reset token via email (you can customize this email template)
	    emailService.sendPasswordResetEmail(user.get().getEmail(), resetToken);

	    return ResponseEntity.ok().body("Password reset link sent to your email");
	}

//	*********************** Reset Password *************************
	@PostMapping("/reset-password")
	public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestBody PasswordResetRequest request) {
        UIResponse response = new UIResponse();
	    if (request.getPassword() == null || request.getPassword().isEmpty()) {
		    response.setResult(false);
		    response.setMessage("New password is required");
		    return ResponseEntity.ok().body(response);
	    }

	    // Validate the token and extract email
	    String email = JwtProvider.validatePasswordResetToken(token);
	    if (email == null) {
		    response.setResult(false);
		    response.setMessage("Invalid or expired token");
		    return ResponseEntity.ok().body(response);
	    }

	    // Fetch user from database
	    Optional<Users> user = userRepository.findByEmail(email);
	    if (user == null) {
		    response.setResult(false);
		    response.setMessage("User not found");
		    return ResponseEntity.ok().body(response);
	    }

	    // Update the password
	    user.get().setPassword(passwordEncoder.encode(request.getPassword()));
	    userRepository.save(user.get());

	    // ✅ Send email notification
	    try {
	        emailService.sendPasswordResetConfirmationEmail(request.getEmail(), user.get().getUsername());
	    } catch (Exception e) {
	        LOG.error("❌ Failed to send password reset confirmation email: " + e.getMessage());
	        // Not failing the request, just logging it
	    }

	    response.setResult(true);
	    response.setMessage("Password reset successfully");
	    return ResponseEntity.ok().body(response);
	}




	private Authentication authenticate(String username, String password) {
	    UserDetails userDetails = customUserService.loadUserByUsername(username);
	    if (userDetails == null) {
	        throw new BadCredentialsException("Invalid username or password");
	    }
	    if (!passwordEncoder.matches(password, userDetails.getPassword())) {
	        throw new BadCredentialsException("Invalid username or password");
	    }
	    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}

	
	
	
	
	  private static final String[] IP_HEADERS = {
		        "X-Forwarded-For",
		        "Proxy-Client-IP",
		        "WL-Proxy-Client-IP",
		        "HTTP_X_FORWARDED_FOR",
		        "HTTP_X_FORWARDED",
		        "HTTP_X_CLUSTER_CLIENT_IP",
		        "HTTP_CLIENT_IP",
		        "HTTP_FORWARDED_FOR",
		        "HTTP_FORWARDED",
		        "HTTP_VIA",
		        "REMOTE_ADDR"

		        // you can add more matching headers here ...
		    };
	   public static String getRequestIP(HttpServletRequest request) {
	        for (String header: IP_HEADERS) {
	            String value = request.getHeader(header);
	            if (value == null || value.isEmpty()) {
	                continue;
	            }
	            String[] parts = value.split("\\s*,\\s*");
	            return parts[0];
	        }
	        return request.getRemoteAddr();
	    }
	   
	   
	   public Users findUserByEmailOrUsername(String input) {
		    LOG.info("Attempting to find user by input: {}", input);

		    Optional<Users> user = userRepository.findByEmail(input);
		    if (user != null) {
		        LOG.info("User found by email.");
		        return user.get();
		    }

		    user = userRepository.findByUsername(input);
		    if (user != null) {
		        LOG.info("User found by username.");
		        return user.get();
		    }

		    LOG.warn("No user found for input: {}", input);
		    return null;
		}


}
