package com.shipping.demo.interfaces;

import com.shipping.demo.domain.model.Quote;
import com.shipping.demo.domain.service.OptionsService;
import com.shipping.demo.domain.service.ShippingService;
import com.shipping.demo.infrastructure.repository.QuoteRepository;
import com.shipping.demo.interfaces.model.ShippingOptionDTO;
import com.shipping.demo.interfaces.request.ShippingRequest;
import com.shipping.demo.interfaces.response.ShippingResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ShippingController {

    private final ShippingService shippingService;
    private final OptionsService optionsService;
    private final QuoteRepository quoteRepository;

    @PostMapping(
            path = "/shipping",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ShippingResponse> calculate(@Valid @RequestBody ShippingRequest request) {
        return ResponseEntity.ok(shippingService.calculate(request));
    }

    @GetMapping(path = "/shipping/options", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ShippingOptionDTO> options() {
        return optionsService.getAllOptions();
    }

    @GetMapping(path = "/shipping/quotes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Quote>> listQuotes() {
        return ResponseEntity.ok(quoteRepository.findAll());
    }
}
