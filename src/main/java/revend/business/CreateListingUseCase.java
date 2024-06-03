package revend.business;

import revend.domain.CreateListingRequest;
import revend.domain.CreateListingResponse;

public interface CreateListingUseCase {
    CreateListingResponse createListing(CreateListingRequest request);
}
