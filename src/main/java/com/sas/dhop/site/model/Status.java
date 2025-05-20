package com.sas.dhop.site.model;

import jakarta.persistence.Column;
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
@Table(name = "status")
public class Status extends AbstractEntity<Integer> implements Serializable {

    @Column(name = "status_name", length = 100)
    private String statusName;

    @Column(name = "status_type")
    private String statusType;

    @Column(name = "description")
    private String description;

}