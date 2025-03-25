package com.creage.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "application")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_vacancy_id", nullable = false)
    private JobVacancy jobVacancy;

    @ManyToOne
    @JoinColumn(name = "applicant_id", nullable = false)
    private JobApplicant applicant;

    private LocalDateTime appliedAt = LocalDateTime.now();
    private String status; // e.g., Applied, Under Review, Interview Scheduled, Offered, Rejected
}
