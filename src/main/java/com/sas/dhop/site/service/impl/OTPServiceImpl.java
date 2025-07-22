package com.sas.dhop.site.service.impl;

import com.sas.dhop.site.exception.BusinessException;
import com.sas.dhop.site.exception.ErrorConstant;
import com.sas.dhop.site.model.nosql.OTP;
import com.sas.dhop.site.repository.nosql.OTPRepository;
import com.sas.dhop.site.service.OTPService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "[OTP Service]")
public class OTPServiceImpl implements OTPService {
    @NonFinal
    private static final Long OTP_EXPIRATION_MINUTES = 5L;

    private final OTPRepository otpRepository;
    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;

    @Override
    public String generateOTP(String email) {
        String otpCode = String.valueOf(100000 + new Random().nextInt(900000));

        otpRepository.save(OTP.builder()
                .email(email)
                .otpCode(otpCode)
                .expiresAt(LocalDateTime.now().plusMinutes(OTP_EXPIRATION_MINUTES))
                .isVerify(false)
                .build());

        log.info("Generated OTP [{}] for email [{}]", otpCode, email);

        return otpCode;
    }

    @Override
    public CompletableFuture<Boolean> sendOTPByEmail(String email, String name, String OTP) throws MessagingException {
        String subject = "Vui lòng xác nhận OTP của bạn";

        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("OTP", OTP);
        context.setVariable("OTP_EXPIRATION_MINUTES", OTP_EXPIRATION_MINUTES);

        String body = templateEngine.process("email", context);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(body, true);

        javaMailSender.send(message);
        log.info("Email sent to: {}", email);
        return CompletableFuture.completedFuture(true);
    }

    @Override
    public void validateOTP(String email, String OTP) {
        OTP otp = otpRepository.findByOtpCode(OTP).orElseThrow(() -> new BusinessException(ErrorConstant.INVALID_OTP));
        if (!otp.getEmail().equals(email)) {
            throw new BusinessException(ErrorConstant.INVALID_OTP);
        }
        otpRepository.delete(otp);
    }
}
