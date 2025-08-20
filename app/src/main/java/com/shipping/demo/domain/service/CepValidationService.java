package com.shipping.demo.domain.service;

import com.shipping.demo.infrastructure.http.ViaCepClient;
import com.shipping.demo.shared.exception.BusinessException;
import org.springframework.stereotype.Service;

@Service
public class CepValidationService {

    private static final String ERR_BOTH_ZIPS = "Failed to validate both origin and destination ZIP codes";
    private static final String ERR_ORIGIN_ZIP = "Invalid or not found origin ZIP code";
    private static final String ERR_DESTINATION_ZIP = "Invalid or not found destination ZIP code";

    private final ViaCepClient viaCepClient;

    public CepValidationService(ViaCepClient viaCepClient) {
        this.viaCepClient = viaCepClient;
    }

    public void validateOriginAndDestinationAsync(String origin, String dest) {
        var originFuture = viaCepClient.isValidCepAsync(origin);
        var destFuture = viaCepClient.isValidCepAsync(dest);

        boolean originOk = originFuture.handle((ok, ex) -> ex == null && Boolean.TRUE.equals(ok)).join();
        boolean destOk   = destFuture.handle((ok, ex) -> ex == null && Boolean.TRUE.equals(ok)).join();

        if (!originOk && !destOk) {
            throw new RuntimeException(ERR_BOTH_ZIPS);
        }
        if (!originOk) {
            throw new BusinessException(ERR_ORIGIN_ZIP);
        }
        if (!destOk) {
            throw new BusinessException(ERR_DESTINATION_ZIP);
        }
    }
}
