package com.shipping.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shipping.demo.infrastructure.http.ViaCepClient;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ViaCepClientTest {

    @Test
    void shouldReturnTrueWhenViaCepOkAndNoErrorField() throws Exception {
        // Arrange
        HttpClient httpClient = mock(HttpClient.class);
        @SuppressWarnings("unchecked")
        HttpResponse<String> response = (HttpResponse<String>) mock(HttpResponse.class);

        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn("{\"cep\":\"01001000\",\"logradouro\":\"Praça da Sé\"}");

        when(httpClient.sendAsync(any(), any(HttpResponse.BodyHandler.class)))
                .thenReturn(CompletableFuture.completedFuture(response));

        ViaCepClient client = new ViaCepClient(httpClient, new ObjectMapper());

        // Act
        Boolean ok = client.isValidCepAsync("01001000").join();

        // Assert
        assertTrue(ok);
        verify(httpClient, times(1)).sendAsync(any(), any(HttpResponse.BodyHandler.class));
    }

    @Test
    void shouldReturnFalseWhenViaCepRespondsErrorTrue() throws Exception {
        HttpClient httpClient = mock(HttpClient.class);
        @SuppressWarnings("unchecked")
        HttpResponse<String> response = (HttpResponse<String>) mock(HttpResponse.class);

        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn("{\"erro\": true}");

        when(httpClient.sendAsync(any(), any(HttpResponse.BodyHandler.class)))
                .thenReturn(CompletableFuture.completedFuture(response));

        ViaCepClient client = new ViaCepClient(httpClient, new ObjectMapper());

        Boolean ok = client.isValidCepAsync("99999999").join();

        assertFalse(ok);
    }

    @Test
    void shouldThrowWhenStatusNotOk() throws Exception {
        HttpClient httpClient = mock(HttpClient.class);
        @SuppressWarnings("unchecked")
        HttpResponse<String> response = (HttpResponse<String>) mock(HttpResponse.class);

        when(response.statusCode()).thenReturn(500);
        when(response.body()).thenReturn("oops");

        when(httpClient.sendAsync(any(), any(HttpResponse.BodyHandler.class)))
                .thenReturn(CompletableFuture.completedFuture(response));

        ViaCepClient client = new ViaCepClient(httpClient, new ObjectMapper());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> client.isValidCepAsync("01001000").join());
        assertTrue(ex.getMessage().contains("ViaCEP HTTP status"));
    }

    @Test
    void shouldShortCircuitOnInvalidZip() {
        ViaCepClient client = new ViaCepClient();
        assertFalse(client.isValidCepAsync("ABC").join());
        assertFalse(client.isValidCepAsync(null).join());
        assertFalse(client.isValidCepAsync("123").join());
    }
}