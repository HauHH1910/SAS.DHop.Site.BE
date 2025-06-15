package com.sas.dhop.site.model;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "performance")
public class Performance extends AbstractEntity<Integer> implements Serializable {

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "media_url")
	private String mediaUrl;

	@Column(name = "media_type")
	private String mediaType;

	@Column(name = "purpose")
	private String purpose;

	@Column(name = "description")
	private String description;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "status_id", nullable = false)
	private Status status;
}
