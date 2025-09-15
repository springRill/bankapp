package com.exchange.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class CustomMetrics {

    private final MeterRegistry meterRegistry;

    public CustomMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void incrementCurrencyUpdate() {
        meterRegistry.counter("currency_update_total").increment();
    }

}
