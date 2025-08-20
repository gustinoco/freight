package com.shipping.demo.interfaces.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class DestinationDTO {

    private static final String ZIP_REQUIRED_MSG = "Destination ZIP code is required";
    private static final String ZIP_PATTERN = "\\d{8}";
    private static final String ZIP_PATTERN_MSG = "Destination ZIP code must have 8 numeric digits";

    @NotBlank(message = ZIP_REQUIRED_MSG)
    @Pattern(regexp = ZIP_PATTERN, message = ZIP_PATTERN_MSG)
    private String postalCode;
}
