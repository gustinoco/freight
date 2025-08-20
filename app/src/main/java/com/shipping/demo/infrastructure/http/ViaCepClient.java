package com.shipping.demo.infrastructure.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Component
public class ViaCepClient {

    private static final String VIA_CEP_URL_TEMPLATE = "https://viacep.com.br/ws/%s/json/";
    private static final String HEADER_ACCEPT = "Accept";
    private static final String APPLICATION_JSON = "application/json";
    private static final String JSON_ERROR_FIELD = "erro";
    private static final String EX_MSG_VIACEP_STATUS = "ViaCEP HTTP status: ";
    private static final String EX_MSG_VIACEP_PARSE = "Failed to parse ViaCEP response";
    private static final String ZIP_REGEX = "\\d{8}";
    private static final int HTTP_OK = 200;
    private static final long CONNECT_TIMEOUT_SECONDS = 3;
    private static final long REQUEST_TIMEOUT_SECONDS = 2;
    private static final long FUTURE_TIMEOUT_SECONDS = 3;

    private final HttpClient httpClient;
    private final ObjectMapper mapper;

    public ViaCepClient() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(CONNECT_TIMEOUT_SECONDS))
                .build();
        this.mapper = new ObjectMapper();
    }

    public ViaCepClient(HttpClient httpClient, ObjectMapper mapper) {
        this.httpClient = httpClient;
        this.mapper = mapper != null ? mapper : new ObjectMapper();
    }

    public CompletableFuture<Boolean> isValidCepAsync(String cep) {
        if (cep == null || !cep.matches(ZIP_REGEX)) {
            return CompletableFuture.completedFuture(false);
        }

        String url = String.format(VIA_CEP_URL_TEMPLATE, cep);
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .GET()
                .header(HEADER_ACCEPT, APPLICATION_JSON)
                .timeout(Duration.ofSeconds(REQUEST_TIMEOUT_SECONDS))
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .orTimeout(FUTURE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .thenApply(resp -> {
                    if (resp.statusCode() != HTTP_OK) {
                        throw new RuntimeException(EX_MSG_VIACEP_STATUS + resp.statusCode());
                    }
                    try {
                        JsonNode node = mapper.readTree(resp.body());
                        if (node.has(JSON_ERROR_FIELD) && node.get(JSON_ERROR_FIELD).asBoolean()) {
                            return false;
                        }
                        return true;
                    } catch (Exception e) {
                        throw new RuntimeException(EX_MSG_VIACEP_PARSE, e);
                    }
                });
    }
}