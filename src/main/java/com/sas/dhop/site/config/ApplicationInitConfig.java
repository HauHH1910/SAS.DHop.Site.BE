package com.sas.dhop.site.config;

import static com.sas.dhop.site.constant.AreaStatus.ACTIVATED_AREA;
import static com.sas.dhop.site.constant.ChoreographerStatus.ACTIVATED_CHOREOGRAPHER;
import static com.sas.dhop.site.constant.DancerStatus.ACTIVATED_DANCER;
import static com.sas.dhop.site.constant.UserStatus.ACTIVE_USER;
import static com.sas.dhop.site.constant.UserSubscriptionStatus.*;

import com.github.javafaker.Faker;
import com.sas.dhop.site.constant.*;
import com.sas.dhop.site.model.*;
import com.sas.dhop.site.model.enums.RoleName;
import com.sas.dhop.site.model.enums.StatusType;
import com.sas.dhop.site.repository.*;
import jakarta.transaction.Transactional;
import java.util.*;
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

    @Bean
    @Transactional
    ApplicationRunner applicationRunner(
            UserRepository userRepository,
            RoleRepository roleRepository,
            StatusRepository statusRepository,
            AreaRepository areaRepository,
            DanceTypeRepository danceTypeRepository,
            DancerRepository dancerRepository,
            ChoreographyRepository choreographyRepository,
            PasswordEncoder passwordEncoder,
            PerformanceRepository performanceRepository) {
        return args -> {
            Role adminRole = buildRole(roleRepository, RoleName.ADMIN);
            Status adminStatus = buildStatus(statusRepository, RolePrefix.ADMIN_PREFIX);
            User admin = checkIfAdminExistOrNot(userRepository, adminRole, adminStatus, passwordEncoder);

            if (admin != null) {
                Faker fakerVN = new Faker(new Locale("vi"));
                Faker fakerUS = new Faker(new Locale("en-US"));

                Set<String> generatedTitles = new HashSet<>();
                Set<DanceType> danceTypes = new HashSet<>();

                while (danceTypes.size() < 20) {
                    String title = fakerUS.job().title();
                    if (generatedTitles.add(title)) {
                        danceTypes.add(DanceType.builder().type(title).build());
                    }
                }
                danceTypeRepository.saveAll(danceTypes);
                List<DanceType> danceTypeList = new ArrayList<>(danceTypes);

                for (int i = 0; i < 10; i++) {
                    Role userRole = buildRole(roleRepository, RoleName.USER);
                    Role dancerRole = buildRole(roleRepository, RoleName.DANCER);
                    Role choreographyRole = buildRole(roleRepository, RoleName.CHOREOGRAPHY);

                    Status activeStatus = buildStatus(statusRepository, ACTIVE_USER);

                    // Các status subscription mẫu
                    switch (i) {
                        case 1 -> buildStatus(statusRepository, ACTIVE_USER_SUBSCRIPTION);
                        case 2 -> buildStatus(statusRepository, EXPIRE_USER_SUBSCRIPTION);
                        case 3 -> buildStatus(statusRepository, FREE_TRIAL_USER_SUBSCRIPTION);
                        case 4 -> buildStatus(statusRepository, PENDING_USER_SUBSCRIPTION);
                        case 5 -> buildStatus(statusRepository, RENEWING_USER_SUBSCRIPTION);
                    }

                    if (i % 2 == 0) {
                        User userWithRoleDancer =
                                buildUser(userRepository, activeStatus, passwordEncoder, fakerUS, dancerRole);
                        DanceType randomType = getRandomDanceType(danceTypeList);
                        buildDancer(
                                dancerRepository,
                                buildStatus(statusRepository, ACTIVATED_DANCER),
                                userWithRoleDancer,
                                randomType);
                        buildPerformance(performanceRepository, activeStatus, userWithRoleDancer);
                    }

                    buildUser(userRepository, activeStatus, passwordEncoder, fakerUS, userRole);

                    User choreographyUser =
                            buildUser(userRepository, activeStatus, passwordEncoder, fakerUS, choreographyRole);
                    DanceType randomType = getRandomDanceType(danceTypeList);
                    buildChoreography(
                            choreographyRepository,
                            buildStatus(statusRepository, ACTIVATED_CHOREOGRAPHER),
                            choreographyUser,
                            randomType);

                    buildArea(areaRepository, fakerVN, buildStatus(statusRepository, ACTIVATED_AREA));
                }

                log.info("Created default admin user");
            } else {
                log.info("Default admin already init");
            }
        };
    }

    private DanceType getRandomDanceType(List<DanceType> danceTypes) {
        return danceTypes.get(new Random().nextInt(danceTypes.size()));
    }

    private void buildArea(AreaRepository areaRepository, Faker faker, Status status) {
        areaRepository.save(Area.builder()
                .district(faker.address().cityName())
                .ward(faker.address().country())
                .city(faker.address().city())
                .status(status)
                .build());
    }

    private void buildChoreography(
            ChoreographyRepository choreographyRepository, Status status, User user, DanceType type) {
        choreographyRepository.save(Choreography.builder()
                .status(status)
                .user(user)
                .danceTypes(Set.of(type))
                .build());
    }

    private void buildDancer(DancerRepository dancerRepository, Status status, User user, DanceType type) {
        dancerRepository.save(Dancer.builder()
                .status(status)
                .user(user)
                .teamSize(100)
                .danceTypes(Set.of(type))
                .build());
    }

    private void buildPerformance(PerformanceRepository performanceRepository, Status status, User user) {
        performanceRepository.save(
                Performance.builder().user(user).status(status).build());
    }

    private Role buildRole(RoleRepository roleRepository, RoleName role) {
        return roleRepository
                .findByName(role)
                .orElseGet(() -> roleRepository.save(Role.builder().name(role).build()));
    }

    private Status buildStatus(StatusRepository statusRepository, String status) {
        return statusRepository.findByStatusName(status).orElseGet(() -> statusRepository.save(buildStatus(status)));
    }

    private static Status buildStatus(String status) {
        return Status.builder()
                .statusName(status)
                .statusType(StatusType.ACTIVE)
                .description(status)
                .build();
    }

    private static User adminEntity(PasswordEncoder passwordEncoder) {
        return User.builder()
                .name("admin")
                .email("admin@gmail.com")
                .password(passwordEncoder.encode("123456"))
                .build();
    }

    private User buildUser(
            UserRepository userRepository, Status status, PasswordEncoder passwordEncoder, Faker faker, Role role) {
        return userRepository.save(User.builder()
                .status(status)
                .email(faker.internet().emailAddress())
                .password(passwordEncoder.encode("123456"))
                .name(faker.name().fullName())
                .phone(faker.phoneNumber().phoneNumber())
                .roles(Set.of(role))
                .build());
    }

    private User checkIfAdminExistOrNot(
            UserRepository userRepository, Role role, Status status, PasswordEncoder passwordEncoder) {
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
