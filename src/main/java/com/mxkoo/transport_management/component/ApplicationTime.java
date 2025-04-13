package com.mxkoo.transport_management.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ApplicationTime {

    private final Clock clock;

    public LocalDate today() {
        return LocalDate.now(clock);
    }
}
