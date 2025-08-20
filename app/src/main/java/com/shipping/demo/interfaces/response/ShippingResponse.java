package com.shipping.demo.interfaces.response;

import com.shipping.demo.interfaces.model.*;
import lombok.Data;

import java.util.List;

@Data
public class ShippingResponse {

    private OriginDTO origin;
    private DestinationDTO destination;
    private List<ShippingOptionDTO> options;
    private DimensionsDTO dimensions;

    private List<DeliveryOptionDTO> delivery_options;
}
