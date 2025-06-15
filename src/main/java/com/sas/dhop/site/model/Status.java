package com.sas.dhop.site.model;

import com.sas.dhop.site.model.enums.StatusType;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "status")
public class Status extends AbstractEntity<Integer> implements Serializable {

	@Column(name = "status_name", length = 100)
	private String statusName;

	@Column(name = "status_type")
	@Enumerated(EnumType.STRING)
	private StatusType statusType;

	@Column(name = "description")
	private String description;
}
