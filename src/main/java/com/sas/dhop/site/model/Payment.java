package com.sas.dhop.site.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment")
public class Payment extends AbstractEntity<Integer> implements Serializable {

    @Column(name = "order_code")
    private Long orderCode;

    @Column(name = "status")
    private String status;

    @Column(name = "amount")
    private Integer amount;
}
