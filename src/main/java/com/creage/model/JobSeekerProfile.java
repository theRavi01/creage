package com.creage.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.validation.constraints.*;
import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "job_seeker")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobSeekerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users userId; 
    private String firstName;
    private String lastName;
    private String profilePhoto; 
    private String backgroundPhoto; 
    
    @NotBlank(message = "Headline is required")
    private String headline; 
    
    @NotBlank(message = "Current position is required")
    private String currentPosition;
    
    private String education; 
    
    @NotBlank(message = "Location is required")
    private String location;
    private String industry;
    private String aboutMe;
    private boolean openToWork; 

    @ManyToMany
    @JoinTable(
        name = "job_seeker_skills",
        joinColumns = @JoinColumn(name = "job_seeker_profile_id"),
        inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    @JsonManagedReference  // Prevents infinite recursion
    private List<Skill> skills;

    @OneToMany(mappedBy = "jobseekerProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore  // Prevents serialization issues
    private List<Education> educationList;

    @OneToMany(mappedBy = "jobseekerProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Experience> experiences;

    @OneToMany(mappedBy = "jobseekerProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Project> projects;


    @OneToMany(mappedBy = "jobseekerProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Certification> certifications;
    
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    private String resumeLink; 
    private String portfolioLink; 
    
    @Email(message = "Invalid email format")
    private String contactEmail;
    
    @Pattern(regexp = "^\\d{10}$", message = "Invalid phone number format")
    private String contactPhone;

    private LocalDateTime updatedAt = LocalDateTime.now();
    
}
