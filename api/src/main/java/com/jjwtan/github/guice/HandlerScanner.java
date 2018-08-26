package com.jjwtan.github.guice;

import com.google.inject.Injector;
import org.eclipse.jetty.server.Handler;

import javax.inject.Inject;

public class HandlerScanner extends Scanner<Handler> {
    @Inject
    public HandlerScanner(Injector injector) {
        super(injector, Handler.class);
    }
}
