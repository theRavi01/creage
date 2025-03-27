package com.creage.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.creage.dto.CompanyDTO;
import com.creage.model.Company;
import com.creage.model.JobApplication;
import com.creage.model.JobVacancy;
import com.creage.serviceImpl.CompanyServiceImpl;
import com.creage.serviceImpl.JobApplicationServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyServiceImpl companyService;
    
    

    /**
     * Registers a new company.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerCompany(@Valid @RequestBody CompanyDTO company) {
        try {
            Company registeredCompany = companyService.registerCompany(company);
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredCompany);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    /**
     * Fetches a company by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getCompanyById(@PathVariable Long id) {
        try {
            Company company = companyService.getCompanyById(id);
            return ResponseEntity.ok(company);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }
	
	/**
     * Endpoint to add a job vacancy.
     */
    @PostMapping("/{companyId}/add")
    public ResponseEntity<?> addJobVacancy(@PathVariable Long companyId, @RequestBody JobVacancy jobVacancy) {
        try {
            JobVacancy savedJob = companyService.addJobVacancy(companyId, jobVacancy);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedJob);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
    

}
