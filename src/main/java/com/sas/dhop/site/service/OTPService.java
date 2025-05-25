package com.sas.dhop.site.service;

import jakarta.mail.MessagingException;
import java.util.concurrent.CompletableFuture;

public interface OTPService {

    String generateOTP(String email);

    CompletableFuture<Boolean> sendOTPByEmail(String email, String name, String OTP) throws MessagingException;

    boolean validateOTP(String email, String OTP);
}
