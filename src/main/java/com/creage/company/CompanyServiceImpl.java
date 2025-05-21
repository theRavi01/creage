package com.creage.company;

import java.util.List;

import org.springframework.stereotype.Service;

import com.creage.dto.CompanyDTO;
import com.creage.exception.JobLimitExceededException;
import com.creage.exception.ResourceNotFoundException;
import com.creage.jobvacancy.JobVacancyRepository;
import com.creage.model.Company;
import com.creage.model.CompanyVerification;
import com.creage.model.JobVacancy;
import com.creage.model.Users;
import com.creage.user.UsersRepository;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class CompanyServiceImpl {

    private final CompanyRepository companyRepository;
    private final UsersRepository usersRepository;
    private final JobVacancyRepository jobVacancyRepository;


    @Transactional
    public Company registerCompany(CompanyDTO dto) {
        try {
            Company company;

            if (dto.getId() != null) {
                // Update existing company
                company = companyRepository.findById(dto.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
            } else {
                // Create new company
                company = new Company();
                company.setIsVerified(CompanyVerification.UNVERIFIED);
            }

            // Validate user existence
            Users user = usersRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            // Set company details from DTO
            company.setUser(user);
            company.setName(dto.getName());
            company.setDescription(dto.getDescription());
            company.setIndustry(dto.getIndustry());
            company.setLocation(dto.getLocation());
            company.setWebsite(dto.getWebsite());
            company.setContactEmail(dto.getContactEmail());
            company.setContactPhone(dto.getContactPhone());
            company.setLinkedIn(dto.getLinkedIn());
            company.setTwitter(dto.getTwitter());

            // Save company details
            Company savedCompany = companyRepository.save(company);
            return savedCompany;

        } catch (Exception e) {
            throw new RuntimeException("Error registering/updating company: " + e.getMessage());
        }
    }


    public Company getCompanyById(Long companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
    }
    
    

    /**
     * Adds a job vacancy with a restriction of 3 jobs for free subscription users.
     */
    @Transactional
    public JobVacancy addJobVacancy(Long companyId, JobVacancy jobVacancy) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        // Check if the user has a FREE subscription and already has 3 jobs posted
        if (company.getUser().getSubscription().getPlanType().equalsIgnoreCase("FREE")) {
            List<JobVacancy> existingVacancies = jobVacancyRepository.findByCompany(company);
            if (existingVacancies.size() >= 3) {
                throw new JobLimitExceededException("Free plan users can only post up to 3 job vacancies.");
            }
        }

        jobVacancy.setCompany(company);
        return jobVacancyRepository.save(jobVacancy);
    }
}
