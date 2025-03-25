package com.creage.model;

import java.util.List;

import org.springframework.boot.actuate.autoconfigure.wavefront.WavefrontProperties.Application;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "job_applicant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobApplicant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;
    private String resumeLink; // URL to the applicant's resume
    private String profileSummary; // Brief summary of the applicant's qualifications

    @OneToMany(mappedBy = "applicant")
    private List<JobApplication> applications;  // ✅ Updated reference

}
