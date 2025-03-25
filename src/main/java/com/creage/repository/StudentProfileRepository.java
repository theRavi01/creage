package com.creage.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.creage.model.StudentProfile;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long>{
    Optional<StudentProfile> findByUserId(Long userId);

}
