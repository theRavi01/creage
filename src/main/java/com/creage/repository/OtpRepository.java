package com.creage.repository;


import com.creage.model.Otp;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findByEmailAndOtp(String email, String otp);
    @Modifying
    @Transactional
    @Query("DELETE FROM Otp u WHERE u.email = :email")
    void deleteByEmail(@Param("email") String email);

	boolean existsByEmail(String email);
}
