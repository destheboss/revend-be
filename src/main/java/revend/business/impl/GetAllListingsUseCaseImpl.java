package revend.business.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import revend.business.GetAllListingsUseCase;
import revend.domain.Listing;
import revend.persistence.ListingRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class GetAllListingsUseCaseImpl implements GetAllListingsUseCase {
    private final ListingRepository listingRepository;

    @Override
    public List<Listing> getAllListings() {
        return listingRepository.findAll().stream()
                .map(ListingConverter::convert)
                .toList();
    }
}
