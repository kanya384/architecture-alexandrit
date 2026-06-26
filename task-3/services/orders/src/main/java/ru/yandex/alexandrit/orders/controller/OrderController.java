package ru.yandex.alexandrit.orders.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.alexandrit.orders.client.CalculatorClient;

@RestController
public class OrderController {
    private final CalculatorClient client;

    public OrderController(CalculatorClient client) {
        this.client = client;
    }

    @GetMapping("/order")
    public Double getOrder() {

        return client.getPrice();
    }
}
