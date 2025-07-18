package com.creage.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;

@Data
public class StudentProfileDTO {
    
    private Long id;
    
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "FirstName cannot be empty")
    private String firstName;
    
    @NotBlank(message = "LastName cannot be empty")
    private String lastName;
    
    @NotBlank(message = "Headline cannot be empty")
    private String headline;

    @NotBlank(message = "Current position cannot be empty")
    private String currentPosition;

    @NotBlank(message = "Education details are required")
    private String education;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Industry field cannot be empty")
    private String industry;

    private String aboutMe;

    private int openToWork;

    private List<Long> skillIds; // List of Skill IDs
}
