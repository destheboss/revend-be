package revend.business.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import revend.business.GetListingUseCase;
import revend.business.exception.ListingNotFoundException;
import revend.domain.Listing;
import revend.persistence.ListingRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GetListingUseCaseImpl implements GetListingUseCase {
    private final ListingRepository listingRepository;

    @Override
    public Optional<Listing> getListingById(Long id) {
        return listingRepository.findById(id)
                .map(ListingConverter::convert)
                .map(Optional::of)
                .orElseThrow(() -> new ListingNotFoundException(id));
    }
}
