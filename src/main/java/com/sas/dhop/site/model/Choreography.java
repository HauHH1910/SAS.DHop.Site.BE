package com.sas.dhop.site.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "choreography")
public class Choreography extends AbstractEntity<Integer> implements Serializable {

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "choreography_dance_type",
            joinColumns = @JoinColumn(name = "choreography_id"),
            inverseJoinColumns = @JoinColumn(name = "dance_type_id"))
    private Set<DanceType> danceTypes;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "choreography_work_area_list",
            joinColumns = @JoinColumn(name = "choreography_id"),
            inverseJoinColumns = @JoinColumn(name = "area_id"))
    private Set<Area> areas;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "about")
    private String about;

    @Column(name = "year_experience")
    private Integer yearExperience;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;
}
