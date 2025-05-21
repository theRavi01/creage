package com.creage.company;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.creage.model.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
}
