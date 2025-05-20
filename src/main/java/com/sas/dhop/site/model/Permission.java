package com.sas.dhop.site.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "permission")
public class Permission extends AbstractEntity<Integer> implements Serializable {

    @Column(name = "name")
    private String name;

}