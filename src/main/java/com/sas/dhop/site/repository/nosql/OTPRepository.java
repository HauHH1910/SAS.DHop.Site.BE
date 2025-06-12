package com.sas.dhop.site.repository.nosql;

import com.sas.dhop.site.model.nosql.OTP;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface OTPRepository extends MongoRepository<OTP, String> {

    Optional<OTP> findByOtpCode(String otpCode);
}
