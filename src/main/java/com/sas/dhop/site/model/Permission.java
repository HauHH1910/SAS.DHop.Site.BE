package com.sas.dhop.site.model;

import com.sas.dhop.site.enums.PermissionName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "permission")
public class Permission extends AbstractEntity<Integer> implements Serializable {

    @Column(name = "name")
    private PermissionName name;
}
