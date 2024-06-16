package revend.business.impl;

import revend.business.exception.ImageProcessingException;

import java.util.Base64;

public class ImageDataConversion {
    private static final int MAX_IMAGE_SIZE = 10 * 1024 * 1024;

    private ImageDataConversion() {
        throw new IllegalStateException("Utility class");
    }

    public static byte[] decodeBase64ToBytes(String base64String) throws ImageProcessingException {
        byte[] bytes = Base64.getDecoder().decode(base64String);
        if (bytes.length > MAX_IMAGE_SIZE) {
            throw new ImageProcessingException("Image file is too large. Maximum allowed size is " + MAX_IMAGE_SIZE + " bytes.");
        }
        return bytes;
    }

    public static String encodeBytesToBase64(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }
}
