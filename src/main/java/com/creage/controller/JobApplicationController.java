package com.creage.controller;

import com.creage.model.ApplicationStatus;
import com.creage.model.JobApplication;
import com.creage.serviceImpl.JobApplicationServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job/application")
@RequiredArgsConstructor
public class JobApplicationController {

    private final JobApplicationServiceImpl jobApplicationService;

    /**
     * Apply for a Job
     */
    @PostMapping("/{jobId}/apply/{userId}")
    public ResponseEntity<JobApplication> applyForJob(@PathVariable Long jobId, @PathVariable Long userId) {
        return ResponseEntity.ok(jobApplicationService.applyForJob(jobId, userId));
    }

    /**
     * Update Application Status
     */
    @PutMapping("/{applicationId}/status/{newStatus}")
    public ResponseEntity<JobApplication> updateApplicationStatus(@PathVariable Long applicationId,
                                                                  @PathVariable ApplicationStatus newStatus) {
        return ResponseEntity.ok(jobApplicationService.updateApplicationStatus(applicationId, newStatus));
    }

    /**
     * Get Applications by User
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<JobApplication>> getApplicationsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(jobApplicationService.getApplicationsByUser(userId));
    }

    /**
     * Get Applications by Company
     */
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<JobApplication>> getApplicationsByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(jobApplicationService.getApplicationsByCompany(companyId));
    }
}
