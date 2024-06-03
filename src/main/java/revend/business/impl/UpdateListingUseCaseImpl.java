package revend.business.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import revend.business.UpdateListingUseCase;
import revend.business.exception.ListingNotFoundException;
import revend.business.exception.UnauthorizedDataAccessException;
import revend.configuration.security.token.AccessToken;
import revend.domain.UpdateListingRequest;
import revend.persistence.ListingRepository;
import revend.persistence.entity.ListingEntity;
import revend.persistence.entity.RoleEnum;

@Service
@AllArgsConstructor
public class UpdateListingUseCaseImpl implements UpdateListingUseCase {
    private final ListingRepository listingRepository;
    private final AccessToken requestAccessToken;

    @Override
    public void updateListing(UpdateListingRequest request) {
        ListingEntity listingEntity = listingRepository.findById(request.getId())
                .orElseThrow(() -> new ListingNotFoundException(request.getId()));

        if (!requestAccessToken.hasRole(RoleEnum.ADMIN.name()) &&
                !requestAccessToken.getUserId().equals(listingEntity.getUser().getId())) {
            throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
        }

        listingEntity.setTitle(request.getTitle());
        listingEntity.setDescription(request.getDescription());
        listingEntity.setPrice(request.getPrice());

        listingRepository.save(listingEntity);
    }
}
