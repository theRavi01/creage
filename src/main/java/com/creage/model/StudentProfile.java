package com.creage.model;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "student_profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users userId; 

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
        name = "student_profile_skills",
        joinColumns = @JoinColumn(name = "student_profile_id"),
        inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    @NotEmpty(message = "At least one skill is required")
    private List<Skill> skills;
	

    @OneToMany(mappedBy = "studentProfile", cascade = CascadeType.ALL)
    private List<Education> educationList;

    @OneToMany(mappedBy = "studentProfile", cascade = CascadeType.ALL)
    private List<Experience> experiences;

    @OneToMany(mappedBy = "studentProfile", cascade = CascadeType.ALL)
    private List<Project> projects;

    @OneToMany(mappedBy = "studentProfile", cascade = CascadeType.ALL)
    private List<Certification> certifications;


    private String resumeLink; 
    private String portfolioLink; 
    
    @Email(message = "Invalid email format")
    private String contactEmail;
    
    @Pattern(regexp = "^\\d{10}$", message = "Invalid phone number format")
    private String contactPhone;

    private LocalDateTime updatedAt = LocalDateTime.now();
}

