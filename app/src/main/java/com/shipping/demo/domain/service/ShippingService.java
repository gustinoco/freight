package com.shipping.demo.domain.service;

import com.shipping.demo.domain.model.FreightRate;
import com.shipping.demo.interfaces.request.ShippingRequest;
import com.shipping.demo.interfaces.response.ShippingResponse;
import com.shipping.demo.shared.exception.BusinessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShippingService {

    private final CepValidationService cepValidationService;
    private final FreightRateFinder freightRateFinder;
    private final DeliveryOptionAssembler deliveryOptionAssembler;
    private final QuoteAuditService quoteAuditService;

    public ShippingService(CepValidationService cepValidationService,
                           FreightRateFinder freightRateFinder,
                           DeliveryOptionAssembler deliveryOptionAssembler,
                           QuoteAuditService quoteAuditService) {
        this.cepValidationService = cepValidationService;
        this.freightRateFinder = freightRateFinder;
        this.deliveryOptionAssembler = deliveryOptionAssembler;
        this.quoteAuditService = quoteAuditService;
    }

    public ShippingResponse calculate(ShippingRequest request) {
        String origin = request.getOrigin().getPostalCode();
        String dest = request.getDestination().getPostalCode();

        try {
            // 1) validate origin/destination (async ViaCEP + business rules)
            cepValidationService.validateOriginAndDestinationAsync(origin, dest);

            // 2) fetch freight strictly from DB
            List<FreightRate> rates = freightRateFinder.findFor(origin, dest);

            // 3) delivery options from DB data
            var delivery = deliveryOptionAssembler.fromRates(rates);

            // 4) audit
            quoteAuditService.save(request, origin, dest, true, null);


            ShippingResponse resp = new ShippingResponse();
            resp.setOrigin(request.getOrigin());
            resp.setDestination(request.getDestination());
            resp.setOptions(request.getOptions());
            resp.setDimensions(request.getDimensions());
            resp.setDelivery_options(delivery);
            return resp;

        } catch (BusinessException be) {
            quoteAuditService.save(request, origin, dest, false, be.getMessage());
            throw be;
        } catch (RuntimeException re) {
            quoteAuditService.save(request, origin, dest, false, re.getMessage());
            throw re;
        }
    }
}