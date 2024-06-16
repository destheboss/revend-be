package revend.business.impl;

import revend.business.GetUserUseCase;
import revend.domain.User;
import revend.persistence.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GetUserUseCaseImpl implements GetUserUseCase {
    private UserRepository userRepository;

    @Override
    public Optional<User> getUser(Long id) {
        return userRepository.findById(id).map(UserConverter::convert);
    }
}
