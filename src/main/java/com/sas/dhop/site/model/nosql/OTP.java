package com.sas.dhop.site.model.nosql;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "otp")
public class OTP {

    @Id
    private String id;

    private String email;

    private String otpCode;

    private Boolean isVerify;

    private LocalDateTime expiresAt;
}
