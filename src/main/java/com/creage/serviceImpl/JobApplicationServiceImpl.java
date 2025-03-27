package com.creage.serviceImpl;

import com.creage.exception.ResourceNotFoundException;
import com.creage.model.*;
import com.creage.repository.JobApplicationRepository;
import com.creage.repository.JobSeekerProfileRepository;
import com.creage.repository.JobVacancyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobApplicationServiceImpl {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final JobVacancyRepository jobVacancyRepository;

    /**
     * Apply for a Job
     */
    @Transactional
    public JobApplication applyForJob(Long jobId, Long userId) {
        JobVacancy jobVacancy = jobVacancyRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));

        JobSeekerProfile jobSeeker = jobSeekerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Job Seeker profile not found for userId: " + userId));

        // Check if user has already applied for the job
        Optional<JobApplication> existingApplication = jobApplicationRepository
                .findByApplicant_IdAndJobVacancy_Id(jobSeeker.getId(), jobVacancy.getId());

        if (existingApplication.isPresent()) {
            throw new IllegalStateException("You have already applied for this job.");
        }

        JobApplication jobApplication = JobApplication.builder()
                .jobVacancy(jobVacancy)
                .applicant(jobSeeker)
                .status(ApplicationStatus.APPLIED)
                .appliedAt(LocalDateTime.now())
                .build();

        return jobApplicationRepository.save(jobApplication);
    }

    /**
     * Update Application Status
     */
    @Transactional
    public JobApplication updateApplicationStatus(Long applicationId, ApplicationStatus newStatus) {
        JobApplication application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + applicationId));

        application.setStatus(newStatus);
        return jobApplicationRepository.save(application);
    }

    /**
     * Get Applications by User
     */
    public List<JobApplication> getApplicationsByUser(Long userId) {
        return jobApplicationRepository.findByApplicant_UserId_Id(userId);
    }

    /**
     * Get Applications by Company
     */
    public List<JobApplication> getApplicationsByCompany(Long companyId) {
        return jobApplicationRepository.findByJobVacancy_Company_Id(companyId);
    }
}
