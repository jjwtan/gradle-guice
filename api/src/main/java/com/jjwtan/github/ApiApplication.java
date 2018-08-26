package com.jjwtan.github;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import com.jjwtan.github.guice.EventListenerScanner;
import com.jjwtan.github.guice.HandlerScanner;
import com.jjwtan.github.modules.JettyModule;
import com.jjwtan.github.modules.ResourceModule;
import com.jjwtan.github.modules.RestEasyModule;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public class ApiApplication {

    private final GuiceFilter filter;
    private final EventListenerScanner eventListenerScanner;
    private final HandlerScanner handlerScanner;

    @Inject
    public ApiApplication(GuiceFilter filter, EventListenerScanner eventListenerScanner, HandlerScanner handlerScanner){
        this.filter = filter;
        this.eventListenerScanner = eventListenerScanner;
        this.handlerScanner = handlerScanner;
    }

    public static void main(String[] args) throws Exception{
        final Injector injector = Guice.createInjector(new JettyModule(), new RestEasyModule("/api"), new ResourceModule());
        injector.getInstance(ApiApplication.class).run();
    }

    public void run() throws Exception {
        final int port = 8080;
        final Server server = new Server(port);

        final ServletContextHandler context = new ServletContextHandler(server, "/");
        FilterHolder filterHolder = new FilterHolder(filter);
        context.addFilter(filterHolder, "/api/*", null);

        // set up the default servlet at "/"
        final ServletHolder defaultServlet = new ServletHolder(new DefaultServlet());
        context.addServlet(defaultServlet, "/");

        eventListenerScanner.accept((listener)->{
            context.addEventListener(listener);
        });

        final HandlerCollection handlers = new HandlerCollection();
        // The Application context is currently the server handler, add it to the list.
        handlers.addHandler(server.getHandler());

        handlerScanner.accept((handler) -> {
            handlers.addHandler(handler);
        });

        server.setHandler(handlers);
        server.start();
        server.join();
    }
}
