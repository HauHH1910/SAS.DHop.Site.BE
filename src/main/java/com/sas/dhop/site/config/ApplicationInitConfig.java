package com.sas.dhop.site.config;

import com.github.javafaker.Faker;
import com.sas.dhop.site.model.*;
import com.sas.dhop.site.model.enums.RoleName;
import com.sas.dhop.site.model.enums.StatusType;
import com.sas.dhop.site.repository.*;
import jakarta.transaction.Transactional;

import java.util.HashSet;
import java.util.Locale;
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
            StatusRepository statusRepository,
            AreaRepository areaRepository,
            DanceTypeRepository danceTypeRepository,
            DancerRepository dancerRepository,
            ChoreographyRepository choreographyRepository, PerformanceRepository performanceRepository) {
        return args -> {
            Role role = roleRepository.findByName(RoleName.ADMIN).orElseGet(() -> {
                Role r = Role.builder().name(RoleName.ADMIN).build();
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
                User admin = User.builder()
                        .name("admin")
                        .email("admin@gmail.com")
                        .password(passwordEncoder.encode("123456"))
                        .roles(roles)
                        .status(status)
                        .build();
                userRepository.save(admin);
                Faker fakerVN = new Faker(new Locale("vi"));
                Faker fakerUS = new Faker(new Locale("en-US"));

                for (int i = 0; i < 10; i++) {
                    Role userRole = roleRepository.findByName(RoleName.USER).orElseGet(() -> {
                        Role r = Role.builder().name(RoleName.USER).build();
                        return roleRepository.save(r);
                    });

                    Role dancerRole = roleRepository.findByName(RoleName.DANCER).orElseGet(() -> {
                        Role r = Role.builder().name(RoleName.DANCER).build();
                        return roleRepository.save(r);
                    });

                    Role choreographyRole = roleRepository.findByName(RoleName.CHOREOGRAPHY).orElseGet(() -> {
                        Role r = Role.builder().name(RoleName.CHOREOGRAPHY).build();
                        return roleRepository.save(r);
                    });
                    Status activeStatus = statusRepository
                            .findByStatusName("Kích hoạt")
                            .orElseGet(() -> statusRepository.save(Status.builder()
                                    .statusName("Kích hoạt")
                                    .description("Kích hoạt")
                                    .statusType(StatusType.ACTIVE)
                                    .build()));

                    DanceType type = danceTypeRepository.save(DanceType.builder()
                            .type(fakerUS.dragonBall().character())
                            .build());

                    if (i % 2 == 0) {
                        // Tạo user với role là DANCER
                        User userWithRoleDancer = userRepository.save(User.builder()
                                .status(activeStatus)
                                .email(fakerUS.internet().emailAddress())
                                .password(passwordEncoder.encode("123456"))
                                .name(fakerUS.name().fullName())
                                .phone(fakerUS.phoneNumber().phoneNumber())
                                .roles(Set.of(dancerRole))
                                .build());

                        dancerRepository.save(Dancer.builder()
                                .status(activeStatus)
                                .user(userWithRoleDancer)
                                .danceTypes(Set.of(type))
                                .build());

                        performanceRepository.save(Performance.builder()
                                .user(userWithRoleDancer)
                                .status(activeStatus)
                                .build());
                    }

                    // Tạo user với role là USER
                    userRepository.save(User.builder()
                            .status(activeStatus)
                            .email(fakerUS.internet().emailAddress())
                            .password(passwordEncoder.encode("123456"))
                            .name(fakerUS.name().fullName())
                            .phone(fakerUS.phoneNumber().phoneNumber())
                            .roles(Set.of(userRole))
                            .build());

                    //Tạo user với role CHOREOGRAPHY
                    User choreographyUser = userRepository.save(User.builder()
                            .status(activeStatus)
                            .email(fakerUS.internet().emailAddress())
                            .password(passwordEncoder.encode("123456"))
                            .name(fakerUS.name().fullName())
                            .phone(fakerUS.phoneNumber().phoneNumber())
                            .roles(Set.of(choreographyRole))
                            .build());

                    choreographyRepository.save(Choreography.builder()
                            .status(activeStatus)
                            .user(choreographyUser)
                            .danceTypes(Set.of(type))
                            .build());


                    areaRepository.save(Area.builder()
                            .district(fakerVN.address().cityName())
                            .ward(fakerVN.address().country())
                            .city(fakerVN.address().city())
                            .status(activeStatus)
                            .build());


                }
                log.info("Created default admin user");
            }
        };
    }
}
