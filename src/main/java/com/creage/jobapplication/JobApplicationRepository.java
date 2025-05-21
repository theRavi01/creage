package com.creage.jobapplication;

import com.creage.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByApplicant_UserId_Id(Long userId);
    List<JobApplication> findByJobVacancy_Company_Id(Long companyId);
    Optional<JobApplication> findByApplicant_IdAndJobVacancy_Id(Long applicantId, Long jobVacancyId);
}
