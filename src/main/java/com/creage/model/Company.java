package com.creage.model;

import java.util.List;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "company")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user; 

    private String name;
    private String description;
    private String industry;
    private String location;
    private String website;
    private String contactEmail;
    private String contactPhone;
    private String linkedIn;
    private String twitter;

    @Enumerated(EnumType.STRING)
    private CompanyVerification isVerified;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<JobVacancy> jobVacancies;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StudentProfile> placedStudents;
}