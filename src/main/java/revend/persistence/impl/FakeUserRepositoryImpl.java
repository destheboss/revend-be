package revend.persistence.impl;

import org.springframework.stereotype.Repository;
import revend.persistence.UserRepository;
import revend.persistence.entity.UserEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class FakeUserRepositoryImpl implements UserRepository {
    private static long NEXT_ID = 1;

    private final List<UserEntity> users;

    public FakeUserRepositoryImpl() {this.users = new ArrayList<>();}

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return this.users
                .stream()
                .filter(userEntity -> userEntity.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public UserEntity save(UserEntity user) {
        if (user.getId() == null) {
            user.setId(NEXT_ID);
            NEXT_ID++;
            this.users.add(user);
        }
        return user;
    }

    @Override
    public void deleteByEmail(String email) {
        this.users.removeIf(userEntity -> userEntity.getEmail().equals(email));
    }

    @Override
    public List<UserEntity> findAll() { return Collections.unmodifiableList(this.users); }

    @Override
    public boolean existsByEmail(String email) {
        return this.users
                .stream()
                .anyMatch(userEntity -> userEntity.getEmail().equals(email));
    }
}
