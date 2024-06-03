package revend.business.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import revend.business.CreateListingUseCase;
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



        ListingEntity newListing = ListingEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getPrice())
                .user(userEntity)
                .build();

        ListingEntity savedListing = listingRepository.save(newListing);

        return CreateListingResponse.builder()
                .listingId(savedListing.getId())
                .build();
    }
}
