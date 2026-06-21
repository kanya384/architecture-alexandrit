package ru.yandex.alexandrit.orders.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class CalculatorClient {
    @Value("${api.calculator.uri}")
    private String calculatorUri;

    private final RestClient restClient;

    public Double getPrice() {
        return restClient.
                get()
                .uri(calculatorUri)
                .retrieve()
                .body(Double.class);
    }
}
