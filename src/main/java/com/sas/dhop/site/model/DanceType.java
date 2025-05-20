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
@Table(name = "dance_type")
public class DanceType extends AbstractEntity<Integer> implements Serializable {

    @Column(name = "type", length = 100)
    private String type;

    @Column(name = "description")
    private String description;
}