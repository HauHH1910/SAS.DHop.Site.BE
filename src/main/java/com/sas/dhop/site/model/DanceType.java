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
@Table(name = "dance_type")
public class DanceType extends AbstractEntity<Integer> implements Serializable {

    @Column(name = "type", length = 100, unique = true)
    private String type;

    @Column(name = "description")
    private String description;
}
