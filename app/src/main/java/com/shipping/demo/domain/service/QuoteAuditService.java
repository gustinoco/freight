package com.shipping.demo.domain.service;

import com.shipping.demo.domain.model.Quote;
import com.shipping.demo.infrastructure.repository.QuoteRepository;
import com.shipping.demo.interfaces.request.ShippingRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuoteAuditService {

    private static final String CSV_SEPARATOR = ",";

    private final QuoteRepository quoteRepository;

    public QuoteAuditService(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(ShippingRequest request,
                     String origin,
                     String dest,
                     boolean success,
                     String errorMessage) {
        Quote q = new Quote();
        q.setOriginCep(origin);
        q.setDestinationCep(dest);
        if (request.getDimensions() != null) {
            q.setWidth(request.getDimensions().getWidth());
            q.setHeight(request.getDimensions().getHeight());
            q.setLength(request.getDimensions().getLength());
            q.setWeight(request.getDimensions().getWeight());
        }
        List<String> services = request.getOptions() == null
                ? List.of()
                : request.getOptions().stream()
                    .map(o -> o.getCode() == null ? "" : o.getCode())
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .filter(s -> !s.isEmpty())
                    .distinct()
                    .toList();
        q.setSelectedServices(String.join(CSV_SEPARATOR, services));
        q.setSuccess(success);
        q.setErrorMessage(errorMessage);
        quoteRepository.save(q);
    }
}
