package com.sas.dhop.site.model;

import com.sas.dhop.site.model.enums.RoleName;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

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

}