package com.notifications.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class CustomMetrics {

    private final MeterRegistry meterRegistry;

    public CustomMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void incrementFailureNotifications(String username) {
        meterRegistry.counter("notifications_failure_total", "username", username).increment();
    }

}
