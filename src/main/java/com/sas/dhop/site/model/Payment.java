package com.sas.dhop.site.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment")
public class Payment extends AbstractEntity<Integer> implements Serializable {

    private Long orderCode;

    private String status;

    private Integer amount;

}
