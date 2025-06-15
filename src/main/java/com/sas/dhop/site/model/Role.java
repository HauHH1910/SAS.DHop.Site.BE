package com.sas.dhop.site.model;

import com.sas.dhop.site.model.enums.RoleName;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Set;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role")
public class Role extends AbstractEntity<Integer> implements Serializable {

	@Column(name = "name")
	@Enumerated(EnumType.STRING)
	private RoleName name;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "roles_permissions", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "permission_id"))
	private Set<Permission> permissions;
}
