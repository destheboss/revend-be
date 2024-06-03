package revend.business.impl;

import revend.business.GetUsersUseCase;
import revend.domain.GetAllUsersRequest;
import revend.domain.GetAllUsersResponse;
import revend.domain.User;
import revend.persistence.UserRepository;
import revend.persistence.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class GetUsersUseCaseImpl implements GetUsersUseCase {
    private UserRepository userRepository;
    @Override
    public GetAllUsersResponse getUsers(final GetAllUsersRequest request) {
        List<UserEntity> results;
        results = userRepository.findAll();

        final GetAllUsersResponse response = new GetAllUsersResponse();
        List<User> users = results
                .stream()
                .map(UserConverter::convert)
                .toList();
        response.setUsers(users);

        return response;
    }
}
