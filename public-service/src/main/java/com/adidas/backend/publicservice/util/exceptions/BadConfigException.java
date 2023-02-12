package com.adidas.backend.publicservice.util.exceptions;

/**
 * <p>Custom fast class for bad configurations</p>
 *
 * @author <a href="mailto:andreyharyuk_95@hotmail.com">
 */
public class BadConfigException extends RuntimeException {

    public BadConfigException(String message) {
        super(message);
    }
}
