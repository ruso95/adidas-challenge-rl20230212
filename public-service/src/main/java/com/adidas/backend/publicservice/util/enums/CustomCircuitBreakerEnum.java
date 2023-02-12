package com.adidas.backend.publicservice.util.enums;


import lombok.Getter;

/**
 * <p>Enum that centralizes the different CircuitBreakers defined in the app</p>
 * <p>Names should be the same as decelerated in application.properties</p>
 *
 * @author <a href="mailto:andreyharyuk_95@hotmail.com">
 */
public enum CustomCircuitBreakerEnum {
    MEMBERS_SERVICE(Constants.MEMBERS_SERVICE),
    ;

    CustomCircuitBreakerEnum(String serviceName) {
        this.serviceName = serviceName;
    }

    @Getter
    private final String serviceName;

    /**
     * Static class for annotations usage
     */
    public static class Constants {
        private Constants() {
            throw new IllegalStateException("Constant class");
        }
        public static final String MEMBERS_SERVICE = "MEMBERS_SERVICE";
    }
}
