package com.shipping.demo.domain.service;

import com.shipping.demo.domain.model.FreightRate;
import com.shipping.demo.infrastructure.repository.FreightRateRepository;
import com.shipping.demo.shared.exception.BusinessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FreightRateFinder {

    private static final String ERR_NO_RATE_FOR_PAIR = "No freight rate found for the given origin and destination ZIP codes";

    private final FreightRateRepository freightRateRepository;

    public FreightRateFinder(FreightRateRepository freightRateRepository) {
        this.freightRateRepository = freightRateRepository;
    }

    public List<FreightRate> findFor(String origin, String dest) {
        List<FreightRate> rates = freightRateRepository.findByOriginZipAndDestinationZip(origin, dest);
        if (rates.isEmpty()) {
            throw new BusinessException(ERR_NO_RATE_FOR_PAIR);
        }
        return rates;
    }
}
