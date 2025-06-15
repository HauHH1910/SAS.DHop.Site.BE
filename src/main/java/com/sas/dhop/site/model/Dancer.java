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
@Table(name = "dancer")
public class Dancer extends AbstractEntity<Integer> implements Serializable {

	@JoinTable(name = "dancer_dance_type", joinColumns = @JoinColumn(name = "dancer_id"), inverseJoinColumns = @JoinColumn(name = "dance_type_id"))
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<DanceType> danceTypes;

	@Column(name = "dancer_nick_name")
	private String dancerNickName;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "about")
	private String about;

	@Column(name = "year_experience")
	private Integer yearExperience;

	@Column(name = "team_size")
	private Integer teamSize;

	@Column(name = "price", precision = 10, scale = 2)
	private BigDecimal price;

	@ManyToOne(fetch = FetchType.LAZY)
	private Subscription subscription;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "status_id", nullable = false)
	private Status status;
}
