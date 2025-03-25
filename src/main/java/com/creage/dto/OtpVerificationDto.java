package com.creage.dto;

import com.creage.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class OtpVerificationDto {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "OTP is required")
    private String otp;

    @NotNull(message = "Role is required")
    private Role role;

    @NotBlank(message = "Password is required")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
        message = "Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one digit, and one special character"
    )
    private String password;
}
