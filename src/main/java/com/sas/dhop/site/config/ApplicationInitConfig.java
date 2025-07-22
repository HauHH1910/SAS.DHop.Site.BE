package com.sas.dhop.site.config;

import com.sas.dhop.site.constant.*;
import com.sas.dhop.site.model.*;
import com.sas.dhop.site.model.enums.RoleName;
import com.sas.dhop.site.model.enums.StatusType;
import com.sas.dhop.site.repository.*;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.*;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.sas.dhop.site.constant.AreaStatus.ACTIVATED_AREA;

@Configuration
@RequiredArgsConstructor
@Slf4j(topic = "[Application Init Configuration]")
public class ApplicationInitConfig {

    @Bean
    @Transactional
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository, StatusRepository statusRepository, AreaRepository areaRepository, DanceTypeRepository danceTypeRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            Role adminRole = buildRole(roleRepository, RoleName.ADMIN);
            Status adminStatus = buildStatus(statusRepository, RolePrefix.ADMIN_PREFIX);
            User admin = checkIfAdminExistOrNot(userRepository, adminRole, adminStatus, passwordEncoder);
            if (admin != null) {
                log.info("Created default admin user");
            } else {
                log.info("Default admin already init");
            }
        };
    }

    private Role buildRole(RoleRepository roleRepository, RoleName role) {
        return roleRepository.findByName(role).orElseGet(() -> roleRepository.save(Role.builder().name(role).build()));
    }

    private Status buildStatus(StatusRepository statusRepository, String status) {
        try {
            return statusRepository.findByStatusName(status).orElseGet(() -> statusRepository.save(Status.builder().statusName(status).statusType(StatusType.ACTIVE).description(status).build()));
        } catch (org.springframework.dao.IncorrectResultSizeDataAccessException e) {
            log.warn("Found duplicate status records for name: {}. Using the first one.", status);
            List<Status> statuses = statusRepository.findAll().stream().filter(s -> status.equals(s.getStatusName())).toList();

            if (!statuses.isEmpty()) {
                return statuses.get(0);
            } else {
                return statusRepository.save(Status.builder().statusName(status).statusType(StatusType.ACTIVE).description(status).build());
            }
        }
    }

    private static User adminEntity(PasswordEncoder passwordEncoder) {
        return User.builder().name("admin").email("admin@gmail.com").password(passwordEncoder.encode("123456")).build();
    }

    private User checkIfAdminExistOrNot(UserRepository userRepository, Role role, Status status, PasswordEncoder passwordEncoder) {
        User user = adminEntity(passwordEncoder);
        if (userRepository.findByEmail(user.getEmail()).isEmpty()) {
            user.setRoles(Set.of(role));
            user.setStatus(status);
            userRepository.save(user);
            return user;
        }
        return null;
    }
}
