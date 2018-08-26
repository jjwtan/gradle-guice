package com.jjwtan.github.modules;

import com.google.inject.AbstractModule;
import com.jjwtan.github.resource.HelloWorld;

public class ResourceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(HelloWorld.class);
    }
}
