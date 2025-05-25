package com.sas.dhop.site.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "otp")
public class OTP extends AbstractEntity<Integer> implements Serializable {

    private String email;

    private String otpCode;

    private LocalDateTime expiresAt;
}
