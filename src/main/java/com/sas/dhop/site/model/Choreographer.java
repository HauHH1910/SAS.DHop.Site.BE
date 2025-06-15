package com.sas.dhop.site.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "choreographer")
@Getter
@Setter
public class Choreographer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "about")
    private String about;

    @Column(name = "year_experience")
    private int yearExperience;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "price")
    private BigDecimal price;

    @ManyToMany
    @JoinTable(
        name = "choreographer_dance_type",
        joinColumns = @JoinColumn(name = "choreographer_id"),
        inverseJoinColumns = @JoinColumn(name = "dance_type_id")
    )
    private List<DanceType> danceTypes;

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;
} 