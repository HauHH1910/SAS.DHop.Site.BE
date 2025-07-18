package com.sas.dhop.site.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "area")
public class Area extends AbstractEntity<Integer> implements Serializable {

    @Size(max = 100) @Column(name = "district", length = 100)
    private String district;

    @Size(max = 100) @Column(name = "ward", length = 100)
    private String ward;

    @Size(max = 100) @Column(name = "city", length = 100)
    private String city;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "status_id")
    private Status status;
}
