package com.shipping.demo.interfaces.request;

import com.shipping.demo.interfaces.model.DestinationDTO;
import com.shipping.demo.interfaces.model.DimensionsDTO;
import com.shipping.demo.interfaces.model.OriginDTO;
import com.shipping.demo.interfaces.model.ShippingOptionDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ShippingRequest {

    @NotNull(message = "origin is required")
    @Valid
    private OriginDTO origin;

    @NotNull(message = "destination is required")
    @Valid
    private DestinationDTO destination;

    private List<ShippingOptionDTO> options;

    @NotNull(message = "dimensions is required")
    @Valid
    private DimensionsDTO dimensions;
}