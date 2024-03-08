package revend.persistence;

import revend.persistence.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<UserEntity> findByEmail(String email);

    UserEntity save(UserEntity user);

    void deleteByEmail(String email);

    List<UserEntity> findAll();

    boolean existsByEmail(String email);
}
