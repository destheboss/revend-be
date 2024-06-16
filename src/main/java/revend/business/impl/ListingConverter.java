package revend.business.impl;

import revend.domain.Listing;
import revend.persistence.entity.ListingEntity;

final class ListingConverter {
    private ListingConverter() {
    }

    public static Listing convert(ListingEntity listingEntity) {
        String imageBase64 = listingEntity.getImageData() != null
                ? ImageDataConversion.encodeBytesToBase64(listingEntity.getImageData())
                : null;

        return Listing.builder()
                .id(listingEntity.getId())
                .title(listingEntity.getTitle())
                .description(listingEntity.getDescription())
                .price(listingEntity.getPrice())
                .category(listingEntity.getCategory())
                .userId(listingEntity.getUser().getId())
                .imageData(imageBase64)
                .build();
    }
}
