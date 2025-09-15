package com.front.metrics;


import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEventsListener {

    private final CustomMetrics customMetrics;

    public AuthenticationEventsListener(CustomMetrics customMetrics) {
        this.customMetrics = customMetrics;
    }

    @EventListener
    public void handleSuccess(AuthenticationSuccessEvent event) {
        String username = event.getAuthentication().getName();
        customMetrics.incrementSuccessLogin(username);
    }

    @EventListener
    public void handleFailure(AbstractAuthenticationFailureEvent event) {
        String username = event.getAuthentication().getName();
        customMetrics.incrementFailureLogin(username);
    }
}
