package revend.business.impl;

import revend.business.DeleteUserUseCase;
import revend.persistence.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor

public class DeleteUserUseCaseImpl implements DeleteUserUseCase {
    private final UserRepository userRepository;

    @Override
    public void deleteUser(String email) {
        this.userRepository.deleteByEmail(email);
    }
}
