package com.sas.dhop.site.repository;

import com.sas.dhop.site.enums.RoleName;
import com.sas.dhop.site.model.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoleRepository extends JpaRepository<Role, Integer>, JpaSpecificationExecutor<Role> {
    Optional<Role> findByName(RoleName roleName);
}
