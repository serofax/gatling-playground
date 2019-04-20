package com.github.serofax.gatling.springbootexample;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    private final ThreadMonitor threadMonitor;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        threadMonitor.startMonitor();
    }
}
