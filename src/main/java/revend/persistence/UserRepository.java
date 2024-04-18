package revend.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import revend.persistence.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    void deleteByEmail(String email);
    boolean existsByEmail(String email);
}
