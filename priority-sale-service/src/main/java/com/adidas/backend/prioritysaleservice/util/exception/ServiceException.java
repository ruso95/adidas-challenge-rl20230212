package com.adidas.backend.prioritysaleservice.util.exception;

/**
 * <p>Custom fast exception for external service errors</p>
 *
 * @author <a href="mailto:andreyharyuk_95@hotmail.com">
 */
public class ServiceException extends RuntimeException {
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
