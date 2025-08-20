package com.shipping.demo.interfaces;

import com.shipping.demo.interfaces.model.DestinationDTO;
import com.shipping.demo.interfaces.model.DimensionsDTO;
import com.shipping.demo.interfaces.model.OriginDTO;
import com.shipping.demo.interfaces.request.ShippingRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ShippingRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        assertNotNull(validator);
    }

    @Test
    void validRequest_shouldPass() {
        ShippingRequest req = validRequest("01001000", "01310930", 10, 5, 20, 2.5);
        Set<ConstraintViolation<ShippingRequest>> violations = validator.validate(req);
        assertTrue(violations.isEmpty(), () -> "Expected no violations, got: " + violations);
    }

    @Test
    void nullOrigin_shouldFail() {
        ShippingRequest req = validRequest("01001000", "01310930", 10, 5, 20, 2.5);
        req.setOrigin(null);

        Set<ConstraintViolation<ShippingRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
        assertTrue(hasPath(violations, "origin"));
    }

    @Test
    void nullDestination_shouldFail() {
        ShippingRequest req = validRequest("01001000", "01310930", 10, 5, 20, 2.5);
        req.setDestination(null);

        Set<ConstraintViolation<ShippingRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
        assertTrue(hasPath(violations, "destination"));
    }

    @Test
    void nullDimensions_shouldFail() {
        ShippingRequest req = validRequest("01001000", "01310930", 10, 5, 20, 2.5);
        req.setDimensions(null);

        Set<ConstraintViolation<ShippingRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
        assertTrue(hasPath(violations, "dimensions"));
    }

    @Test
    void invalidOriginZip_shouldFail() {
        ShippingRequest req = validRequest("ABC12345", "01310930", 10, 5, 20, 2.5);

        Set<ConstraintViolation<ShippingRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
        assertTrue(hasPath(violations, "origin.postalCode"));
    }

    @Test
    void invalidDestinationZip_shouldFail() {
        ShippingRequest req = validRequest("01001000", "000", 10, 5, 20, 2.5);

        Set<ConstraintViolation<ShippingRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
        assertTrue(hasPath(violations, "destination.postalCode"));
    }

    @Test
    void nonPositiveDimensions_shouldFail() {
        ShippingRequest req = validRequest("01001000", "01310930", 0, -1, 0.0, -5);

        Set<ConstraintViolation<ShippingRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
        assertTrue(hasPath(violations, "dimensions.width"));
        assertTrue(hasPath(violations, "dimensions.height"));
        assertTrue(hasPath(violations, "dimensions.length"));
        assertTrue(hasPath(violations, "dimensions.weight"));
    }

    private static ShippingRequest validRequest(String originZip, String destZip,
                                                double w, double h, double l, double weight) {
        OriginDTO origin = new OriginDTO();
        origin.setPostalCode(originZip);

        DestinationDTO dest = new DestinationDTO();
        dest.setPostalCode(destZip);

        DimensionsDTO dim = new DimensionsDTO();
        dim.setWidth(w);
        dim.setHeight(h);
        dim.setLength(l);
        dim.setWeight(weight);

        ShippingRequest req = new ShippingRequest();
        req.setOrigin(origin);
        req.setDestination(dest);
        req.setDimensions(dim);
        return req;
    }

    private static boolean hasPath(Set<? extends ConstraintViolation<?>> violations, String path) {
        return violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals(path));
    }
}