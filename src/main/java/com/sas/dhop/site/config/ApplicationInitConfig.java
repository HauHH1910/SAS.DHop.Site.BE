package com.sas.dhop.site.config;

import com.sas.dhop.site.model.*;
import com.sas.dhop.site.model.enums.RoleName;
import com.sas.dhop.site.model.enums.StatusType;
import com.sas.dhop.site.repository.*;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Slf4j(topic = "[Application Init Configuration]")
public class ApplicationInitConfig {

    private final PasswordEncoder passwordEncoder;

    @Bean
    @Transactional
    ApplicationRunner applicationRunner(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PermissionRepository permissionRepository,
            StatusRepository statusRepository) {
        return args -> {
            Permission permission = permissionRepository.findByName("READ_DATA").orElseGet(() -> {
                Permission p = Permission.builder().name("READ_DATA").build();
                return permissionRepository.save(p);
            });

            Role role = roleRepository.findByName(RoleName.ADMIN).orElseGet(() -> {
                Set<Permission> permissions = new HashSet<>();
                permissions.add(permission);
                Role r = Role.builder()
                        .name(RoleName.ADMIN)
                        .permissions(permissions)
                        .build();
                return roleRepository.save(r);
            });

            Status status = statusRepository.findByStatusName("ADMIN").orElseGet(() -> {
                Status s = Status.builder()
                        .statusName("ADMIN")
                        .statusType(StatusType.ACTIVE)
                        .description("DESCRIPTION")
                        .build();

                return statusRepository.save(s);
            });

            if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {
                Set<Role> roles = new HashSet<>();
                roles.add(role);
                User user = User.builder()
                        .name("admin")
                        .email("admin@gmail.com")
                        .password(passwordEncoder.encode("123456"))
                        .roles(roles)
                        .status(status)
                        .build();
                userRepository.save(user);
                log.info("Created default admin user");
            }
        };
    }
}
