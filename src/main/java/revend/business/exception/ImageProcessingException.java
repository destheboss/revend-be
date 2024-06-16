package revend.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ImageProcessingException extends ResponseStatusException {
    public ImageProcessingException(String errorCode) { super(HttpStatus.BAD_REQUEST, errorCode); }
}
