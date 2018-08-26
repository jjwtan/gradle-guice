package com.jjwtan.github.guice;

import com.google.inject.Injector;

import javax.inject.Inject;
import java.util.EventListener;

public class EventListenerScanner extends Scanner<EventListener> {
    @Inject
    public EventListenerScanner(Injector injector) {
        super(injector, EventListener.class);
    }
}
