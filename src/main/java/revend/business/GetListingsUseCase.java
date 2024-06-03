package revend.business;

import revend.domain.Listing;

import java.util.List;

public interface GetListingsUseCase {
    List<Listing> getListings(Long userId);
}
