package com.creage.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.actuate.autoconfigure.wavefront.WavefrontProperties.Application;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "job_vacancy")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobVacancy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    private String title;
    private String description;
    private String location;
    private String employmentType; // e.g., Full-Time, Part-Time
    private LocalDateTime postedAt = LocalDateTime.now();
    private LocalDateTime applicationDeadline;
    private int isActive; // Indicates if the job is currently accepting applications

    @OneToMany(mappedBy = "jobVacancy")
    private List<JobApplication> applications;  // ✅ Updated reference

}
