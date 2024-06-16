package revend.business.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import revend.business.CreateListingUseCase;
import revend.business.exception.ImageProcessingException;
import revend.business.exception.InvalidUserException;
import revend.domain.CreateListingRequest;
import revend.domain.CreateListingResponse;
import revend.persistence.ListingRepository;
import revend.persistence.UserRepository;
import revend.persistence.entity.ListingEntity;
import revend.persistence.entity.UserEntity;

@Service
@AllArgsConstructor
public class CreateListingUseCaseImpl implements CreateListingUseCase {
    private final ListingRepository listingRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CreateListingResponse createListing(CreateListingRequest request) {
        UserEntity userEntity = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new InvalidUserException("User not found"));

        byte[] imageData = null;
        if (request.getImageBase64() != null && !request.getImageBase64().isEmpty()) {
            try {
                imageData = ImageDataConversion.decodeBase64ToBytes(request.getImageBase64());
            } catch (ImageProcessingException e) {
                throw new ImageProcessingException("Failed to decode image data");
            }
        }

        ListingEntity savedListing = saveNewListing(request, userEntity, imageData);

        return CreateListingResponse.builder()
                .listingId(savedListing.getId())
                .build();
    }

    private ListingEntity saveNewListing(CreateListingRequest request, UserEntity user, byte[] imageData) {
        ListingEntity newListing = ListingEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(request.getCategory())
                .imageData(imageData)
                .user(user)
                .build();

        return listingRepository.save(newListing);
    }
}
