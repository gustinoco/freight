package com.shipping.demo.domain.service;

import com.shipping.demo.domain.model.FreightRate;
import com.shipping.demo.interfaces.model.DestinationDTO;
import com.shipping.demo.interfaces.model.DeliveryOptionDTO;
import com.shipping.demo.interfaces.model.DimensionsDTO;
import com.shipping.demo.interfaces.model.OriginDTO;
import com.shipping.demo.interfaces.model.ShippingOptionDTO;
import com.shipping.demo.interfaces.request.ShippingRequest;
import com.shipping.demo.interfaces.response.ShippingResponse;
import com.shipping.demo.shared.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShippingServiceTest {

    @Mock
    private CepValidationService cepValidationService;

    @Mock
    private FreightRateFinder freightRateFinder;

    @Mock
    private DeliveryOptionAssembler deliveryOptionAssembler;

    @Mock
    private QuoteAuditService quoteAuditService;

    @InjectMocks
    private ShippingService shippingService;

    @Mock
    private ShippingRequest request;

    @Mock
    private OriginDTO originDto;

    @Mock
    private DestinationDTO destinationDto;

    @Mock
    private List<ShippingOptionDTO> optionsDto;

    @Mock
    private DimensionsDTO dimensionsDto;

    @BeforeEach
    void setup() {
        when(request.getOrigin()).thenReturn(originDto);
        when(request.getDestination()).thenReturn(destinationDto);
        lenient().when(request.getOptions()).thenReturn(optionsDto);
        lenient().when(request.getDimensions()).thenReturn(dimensionsDto);
    }

    private void stubPostalCodes(String originZip, String destZip) {
        OriginDTO origin = mock(OriginDTO.class, withSettings().defaultAnswer(invocation -> {
            if ("getPostalCode".equals(invocation.getMethod().getName())) {
                return originZip;
            }
            return RETURNS_DEFAULTS.answer(invocation);
        }));
        DestinationDTO dest = mock(DestinationDTO.class, withSettings().defaultAnswer(invocation -> {
            if ("getPostalCode".equals(invocation.getMethod().getName())) {
                return destZip;
            }
            return RETURNS_DEFAULTS.answer(invocation);
        }));

        when(request.getOrigin()).thenReturn(origin);
        when(request.getDestination()).thenReturn(dest);
    }

    @Test
    void calculate_success_audits_true() {
        String originZip = "01001000";
        String destZip = "20040030";
        stubPostalCodes(originZip, destZip);

        doNothing().when(cepValidationService).validateOriginAndDestinationAsync(originZip, destZip);

        FreightRate rate = mock(FreightRate.class);
        List<FreightRate> rates = List.of(rate);
        when(freightRateFinder.findFor(originZip, destZip)).thenReturn(rates);

        List<DeliveryOptionDTO> deliveryOptions = new ArrayList<>();
        when(deliveryOptionAssembler.fromRates(rates)).thenReturn(deliveryOptions);

        ShippingResponse response = shippingService.calculate(request);

        assertNotNull(response);
        assertEquals(request.getOrigin(), response.getOrigin());
        assertEquals(request.getDestination(), response.getDestination());
        assertEquals(request.getOptions(), response.getOptions());
        assertEquals(request.getDimensions(), response.getDimensions());
        assertEquals(deliveryOptions, response.getDelivery_options());

        verify(cepValidationService).validateOriginAndDestinationAsync(originZip, destZip);
        verify(freightRateFinder).findFor(originZip, destZip);
        verify(deliveryOptionAssembler).fromRates(rates);
        verify(quoteAuditService).save(eq(request), eq(originZip), eq(destZip), eq(true), isNull());

        verifyNoMoreInteractions(quoteAuditService, deliveryOptionAssembler, freightRateFinder, cepValidationService);
    }

    @Test
    void calculate_business_exception_audits_false_with_message() {
        String originZip = "11111111";
        String destZip = "22222222";
        stubPostalCodes(originZip, destZip);

        BusinessException business = new BusinessException("regra de negócio falhou");
        doThrow(business).when(cepValidationService).validateOriginAndDestinationAsync(originZip, destZip);

        BusinessException thrown = assertThrows(BusinessException.class, () -> shippingService.calculate(request));

        assertSame(business, thrown);
        verify(quoteAuditService).save(eq(request), eq(originZip), eq(destZip), eq(false), eq("regra de negócio falhou"));

        verify(freightRateFinder, never()).findFor(anyString(), anyString());
        verify(deliveryOptionAssembler, never()).fromRates(anyList());

        verifyNoMoreInteractions(quoteAuditService, deliveryOptionAssembler, freightRateFinder, cepValidationService);
    }

    @Test
    void calculate_runtime_exception_audits_false_with_message() {
        String originZip = "33333333";
        String destZip = "44444444";
        stubPostalCodes(originZip, destZip);

        doNothing().when(cepValidationService).validateOriginAndDestinationAsync(originZip, destZip);

        RuntimeException boom = new RuntimeException("falha inesperada ao buscar frete");
        when(freightRateFinder.findFor(originZip, destZip)).thenThrow(boom);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> shippingService.calculate(request));

        assertSame(boom, thrown);
        verify(quoteAuditService).save(eq(request), eq(originZip), eq(destZip), eq(false), eq("falha inesperada ao buscar frete"));

        verify(deliveryOptionAssembler, never()).fromRates(anyList());

        verifyNoMoreInteractions(quoteAuditService, deliveryOptionAssembler, freightRateFinder, cepValidationService);
    }
}
