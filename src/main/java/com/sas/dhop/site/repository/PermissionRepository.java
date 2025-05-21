package com.sas.dhop.site.repository;

import com.sas.dhop.site.enums.PermissionName;
import com.sas.dhop.site.model.Permission;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PermissionRepository extends JpaRepository<Permission, Integer>, JpaSpecificationExecutor<Permission> {
    Optional<Permission> findByName(PermissionName permissionName);
}
