package com.sas.dhop.site.repository;

import com.sas.dhop.site.model.Payment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByOrderCode(Long orderCode);
}
