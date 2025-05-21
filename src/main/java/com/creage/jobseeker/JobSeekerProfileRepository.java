package com.creage.jobseeker;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.creage.model.JobSeekerProfile;

public interface JobSeekerProfileRepository extends JpaRepository<JobSeekerProfile, Long>{
    Optional<JobSeekerProfile> findByUserId(Long userId);

}
