package revend.business.impl;

import revend.domain.User;
import revend.persistence.entity.UserEntity;

final class UserConverter {
    private UserConverter() {
    }

    public static User convert(UserEntity user) {
        String imageBase64 = user.getImageData() != null
                ? ImageDataConversion.encodeBytesToBase64(user.getImageData())
                : null;

        return User.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .password(user.getPassword())
                .imageData(imageBase64)
                .build();
    }
}
