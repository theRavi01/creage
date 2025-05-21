package com.creage.jobvacancy;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.creage.model.JobVacancy;
import com.creage.model.Company;

@Repository
public interface JobVacancyRepository extends JpaRepository<JobVacancy, Long> {
    List<JobVacancy> findByCompany(Company company);
}
