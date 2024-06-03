package revend.business.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import revend.business.GetListingsUseCase;
import revend.domain.Listing;
import revend.persistence.ListingRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class GetListingsUseCaseImpl implements GetListingsUseCase {
    private final ListingRepository listingRepository;

    @Override
    public List<Listing> getListings(Long userId) {
        return listingRepository.findAllListingsByUserId(userId).stream()
                .map(ListingConverter::convert)
                .toList();
    }
}
