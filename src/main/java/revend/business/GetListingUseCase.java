package revend.business;

import revend.domain.Listing;

import java.util.Optional;

public interface GetListingUseCase {
    Optional<Listing> getListingById(Long id);
}
