package com.shipping.demo.domain.service;

import com.shipping.demo.domain.model.FreightRate;
import com.shipping.demo.interfaces.model.DeliveryOptionDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeliveryOptionAssembler {

    private static final String DEFAULT_DELIVERY_NAME = "delivery";
    private static final double ZERO_PRICE = 0.0;

    public List<DeliveryOptionDTO> fromRates(List<FreightRate> rates) {
        return rates.stream()
                .map(rate -> new DeliveryOptionDTO(
                        DEFAULT_DELIVERY_NAME,
                        rate.getDeadlineDays(),
                        Math.max(ZERO_PRICE, rate.getPrice())))
                .toList();
    }
}
