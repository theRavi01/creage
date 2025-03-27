package com.creage.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyDTO {

    private Long id;  // Optional for updates

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Company name is required")
    private String name;

    private String description;
    private String industry;
    private String location;

    @Pattern(regexp = "^(http|https)://.*$", message = "Invalid website URL")
    private String website;

    @Email(message = "Invalid email format")
    private String contactEmail;

    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$", message = "Invalid phone number")
    private String contactPhone;

    private String linkedIn;
    private String twitter;

    private String isVerified; // Expecting "VERIFIED" or "UNVERIFIED"

    private List<Long> jobVacancyIds;  // List of job vacancies if needed
    private List<Long> placedStudentIds; // List of placed student IDs
}
