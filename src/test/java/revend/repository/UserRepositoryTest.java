package revend.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import revend.persistence.UserRepository;
import revend.persistence.entity.UserEntity;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private UserEntity createUser(String email, String firstName) {
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName("Doe");
        user.setPassword("password123");
        return entityManager.merge(user);
    }

    @Test
    void findByEmail_whenUserExists() {
        String email = "test@example.com";
        UserEntity expectedUser = createUser(email, "Test");

        Optional<UserEntity> foundUser = userRepository.findByEmail(email);

        assertThat(foundUser).isPresent().contains(expectedUser);
    }

    @Test
    void deleteByEmail_whenUserExists() {
        String email = "delete@example.com";
        createUser(email, "Delete");

        userRepository.deleteByEmail(email);
        Optional<UserEntity> deletedUser = userRepository.findByEmail(email);

        assertThat(deletedUser).isNotPresent();
    }

    @Test
    void existsByEmail_whenUserExists() {
        String email = "exists@example.com";
        createUser(email, "Exists");

        boolean exists = userRepository.existsByEmail(email);

        assertThat(exists).isTrue();
    }

    @Test
    void existsByEmail_whenUserDoesNotExist() {
        String email = "nonexistent@example.com";

        boolean exists = userRepository.existsByEmail(email);

        assertThat(exists).isFalse();
    }
}
