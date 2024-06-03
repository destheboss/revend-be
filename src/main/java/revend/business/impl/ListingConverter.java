package revend.business.impl;

import revend.domain.Listing;
import revend.persistence.entity.ListingEntity;

final class ListingConverter {
    private ListingConverter() {
    }

    public static Listing convert(ListingEntity listingEntity) {
        return Listing.builder()
                .id(listingEntity.getId())
                .title(listingEntity.getTitle())
                .description(listingEntity.getDescription())
                .price(listingEntity.getPrice())
                .userId(listingEntity.getUser().getId())
                .build();
    }
}
