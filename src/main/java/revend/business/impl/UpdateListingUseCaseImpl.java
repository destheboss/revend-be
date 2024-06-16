package revend.business.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import revend.business.UpdateListingUseCase;
import revend.business.exception.ImageProcessingException;
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

        byte[] imageData = null;
        if (request.getImageBase64() != null && !request.getImageBase64().isEmpty()) {
            try {
                imageData = ImageDataConversion.decodeBase64ToBytes(request.getImageBase64());
            } catch (ImageProcessingException e) {
                throw new ImageProcessingException("Failed to decode image data");
            }
        }

        updateFields(request, listingEntity, imageData);
    }

    private void updateFields(UpdateListingRequest request, ListingEntity listing, byte[] imageData) {
        listing.setTitle(request.getTitle());
        listing.setDescription(request.getDescription());
        listing.setPrice(request.getPrice());
        listing.setImageData(imageData);
        listing.setCategory(request.getCategory());

        listingRepository.save(listing);
    }
}
