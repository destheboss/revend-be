package revend.configuration.database;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import revend.persistence.UserRepository;
import revend.persistence.entity.RoleEnum;
import revend.persistence.entity.UserEntity;
import revend.persistence.entity.UserRoleEntity;

import java.util.Set;

@Component
@AllArgsConstructor
public class DatabaseDummyDataInitializer {
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void populateDatabaseInitialDummyData() {
        if (isDatabaseEmpty()) {
            insertAdmin();
            insertUsers();
        }
    }

    private boolean isDatabaseEmpty() {
        return userRepository.count() == 0;
    }

    private void insertUsers() {
        for (int i = 0; i < 3; i++) {
            UserEntity user = UserEntity.builder()
                    .firstName("User" + i)
                    .lastName("Lastname" + i)
                    .email("user" + i + "@example.com")
                    .password(passwordEncoder.encode("12345678"))
                    .build();

            UserRoleEntity userRole = UserRoleEntity.builder()
                    .user(user)
                    .role(RoleEnum.USER)
                    .build();

            user.setUserRoles(Set.of(userRole));

            userRepository.save(user);
        }
    }

    private void insertAdmin() {
        UserEntity admin = UserEntity.builder()
                .firstName("Desislav")
                .lastName("Hristov")
                .email("desislav.hristovv@gmail.com")
                .password(passwordEncoder.encode("12345678"))
                .build();

        UserRoleEntity adminRole = UserRoleEntity.builder()
                .user(admin)
                .role(RoleEnum.ADMIN)
                .build();

        admin.setUserRoles(Set.of(adminRole));

        userRepository.save(admin);
    }
}
