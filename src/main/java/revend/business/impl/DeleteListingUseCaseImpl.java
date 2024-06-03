package revend.business.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import revend.business.DeleteListingUseCase;
import revend.business.exception.ListingNotFoundException;
import revend.business.exception.UnauthorizedDataAccessException;
import revend.configuration.security.token.AccessToken;
import revend.persistence.ListingRepository;
import revend.persistence.entity.ListingEntity;
import revend.persistence.entity.RoleEnum;

@Service
@AllArgsConstructor
public class DeleteListingUseCaseImpl implements DeleteListingUseCase {
    private final ListingRepository listingRepository;
    private final AccessToken requestAccessToken;

    @Override
    public void deleteListing(Long listingId) {
        ListingEntity listingEntity = listingRepository.findById(listingId)
                .orElseThrow(() -> new ListingNotFoundException(listingId));

        if (!requestAccessToken.hasRole(RoleEnum.ADMIN.name()) &&
                !requestAccessToken.getUserId().equals(listingEntity.getUser().getId())) {
            throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
        }

        listingRepository.deleteById(listingId);
    }
}
