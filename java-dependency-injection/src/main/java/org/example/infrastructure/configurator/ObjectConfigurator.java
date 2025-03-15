package org.example.infrastructure.configurator;

import org.example.infrastructure.ApplicationContext;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public interface ObjectConfigurator {

    void configure(Object obj, ApplicationContext context) throws IllegalAccessException, InvocationTargetException, IOException;
}
