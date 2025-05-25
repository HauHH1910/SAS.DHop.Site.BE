package com.sas.dhop.site.repository;

import com.sas.dhop.site.model.OTP;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OTPRepository extends JpaRepository<OTP, Integer> {

    Optional<OTP> findByEmail(String email);
}
