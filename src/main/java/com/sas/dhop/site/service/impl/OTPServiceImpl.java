package com.sas.dhop.site.service.impl;

import com.sas.dhop.site.model.nosql.OTP;
import com.sas.dhop.site.repository.nosql.OTPRepository;
import com.sas.dhop.site.service.EmailService;
import com.sas.dhop.site.service.OTPService;
import jakarta.mail.MessagingException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "[OTP Service]")
public class OTPServiceImpl implements OTPService {
    @NonFinal
    private static final Long OTP_EXPIRATION_MINUTES = 5L;

    private final OTPRepository otpRepository;
    private final EmailService emailService;

    @Override
    public String generateOTP(String email) {

        String otpCode = String.valueOf(100000 + new Random().nextInt(900000));

        OTP otp = OTP.builder()
                .email(email)
                .otpCode(otpCode)
                .expiresAt(LocalDateTime.now().plusMinutes(OTP_EXPIRATION_MINUTES))
                .isVerify(false)
                .build();

        otpRepository.save(otp);

        log.info("Generated OTP [{}] for email [{}]", otpCode, email);

        return otpCode;
    }

    @Override
    public CompletableFuture<Boolean> sendOTPByEmail(String email, String name, String OTP) throws MessagingException {
        String subject = "Mã OTP của bạn là " + OTP;

        String body = String.format(
                "<p>Chào %s,</p>" + "<p>Mã OTP của bạn là: <b>%s</b></p>" + "<p>Mã có hiệu lực trong %d phút.</p>",
                name, OTP, OTP_EXPIRATION_MINUTES);

        return emailService
                .sendEmail(email, subject, body)
                .thenApply(v -> {
                    log.info("OTP send email to {}", email);
                    return true;
                })
                .exceptionally(ex -> {
                    log.error("Failed to send OTP email {}: {}", email, ex.getMessage());
                    return false;
                });
    }

    @Override
    public boolean validateOTP(String email, String OTP) {
        Optional<OTP> otpOpt = otpRepository.findByEmail(email);
        if (otpOpt.isEmpty()) {
            log.warn("No OTP found for email {}", email);
            return false;
        }

        OTP otpEntity = otpOpt.get();

        if (otpEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            log.warn("OTP expired for email {}", email);
            otpRepository.delete(otpEntity);
            return false;
        }

        if (!otpEntity.getIsVerify()) {
            log.warn("OTP is already verify email {}", email);
            return false;
        }

        if (!otpEntity.getOtpCode().equals(OTP)) {
            log.warn("Invalid OTP for email {}", email);
            return false;
        }

        otpRepository.delete(otpEntity);
        log.info("OTP validated successfully for email {}", email);
        return true;
    }
}
