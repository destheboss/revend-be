package revend.business;

import revend.domain.Listing;

import java.util.List;

public interface GetAllListingsUseCase {
    List<Listing> getAllListings();
}
