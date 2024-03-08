package revend.business;

import revend.domain.GetAllUsersRequest;
import revend.domain.GetAllUsersResponse;


public interface GetUsersUseCase {
    GetAllUsersResponse getUsers(GetAllUsersRequest request);
}
