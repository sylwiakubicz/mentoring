package com.example.demo.service;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CounterServiceTest {
    @Test
    void shoulCreateBasicCounterWhenNotPremium() {
        CounterService service = new CounterService(false);
        service.acceptText("Hello hello");
        assertThat(service.getCount("hello")).isEqualTo(2);
    }

    @Test
    void shouldCreatePremiumCounterWhenPremium() {
        CounterService service = new CounterService(true);
        service.acceptText("Hello hello");
        assertThat(service.getCount("Hello")).isEqualTo(1);
        assertThat(service.getCount("hello")).isEqualTo(1);
    }
}
