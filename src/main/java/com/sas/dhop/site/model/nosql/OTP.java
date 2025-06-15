package com.sas.dhop.site.model.nosql;

import java.time.LocalDateTime;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
