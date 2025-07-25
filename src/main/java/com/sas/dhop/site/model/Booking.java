package com.sas.dhop.site.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "booking")
public class Booking extends AbstractEntity<Integer> implements Serializable {

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dancer_id")
    private Dancer dancer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "choreography_id")
    private Choreography choreography;

    @JoinTable(
            name = "booking_dance_type",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "dance_type_id"))
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<DanceType> danceType;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "area_id", nullable = false)
    private Area area;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    @Column(name = "booking_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime bookingDate;

    @Column(name = "start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;

    @Column(name = "address")
    private String address;

    @Lob
    @Column(name = "detail", columnDefinition = "TEXT")
    private String detail;

    @Column(name = "update_booking_date")
    private Instant updateBookingDate;

    @Column(name = "booking_status")
    private String bookingStatus;

    @Column(name = "customer_phone")
    private String customerPhone;

    @Column(name = "number_of_training_sessions")
    private Integer numberOfTrainingSessions;

    @Column(name = "dancer_phone")
    private String dancerPhone;

    @Column(name = "choreography_phone")
    private String choreographyPhone;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "number_of_team_member")
    private Integer numberOfTeamMember;

    @Column(name = "dancer_account_number")
    private String dancerAccountNumber;

    @Column(name = "dancer_bank")
    private String dancerBank;

    @Column(name = "choreography_account_number")
    private String choreographyAccountNumber;

    @Column(name = "choreography_bank")
    private String choreographyBank;
}
