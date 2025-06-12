package com.sas.dhop.site.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalTime;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "booking")
public class Booking extends AbstractEntity<Integer> implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dancer_id", nullable = false)
    private Dancer dancer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "choreography_id", nullable = false)
    private Choreography choreography;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dance_type_id", nullable = false)
    private DanceType danceType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "area_id", nullable = false)
    private Area area;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    @Column(name = "booking_date")
    private Instant bookingDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "address")
    private String address;

    @Column(name = "detail")
    private String detail;

    @Column(name = "update_booking_date")
    private Instant updateBookingDate;

    @Column(name = "booking_status")
    private String bookingStatus;

    @Column(name = "customer_phone")
    private String customerPhone;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "performance_id", nullable = false)
    private Performance performance;

    @Column(name = "number_of_training_sessions", nullable = false)
    private Integer numberOfTrainingSessions;

    @Column(name = "dancer_phone")
    private String dancerPhone;

    @Column(name = "choreography_phone")
    private String choreographyPhone;

    @Column(name = "cancel_reason")
    private String cancelReason;

    @Column(name = "cancel_person_name")
    private String cancelPersonName;

    @Column(name = "price")
    private Long price;
}
