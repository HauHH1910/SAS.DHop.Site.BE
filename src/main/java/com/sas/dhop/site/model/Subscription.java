package com.sas.dhop.site.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subscription")
public class Subscription extends AbstractEntity<Integer> implements Serializable {

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "content")
    private String content;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;
}
