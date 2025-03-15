package org.example.infrastructure.configurator;

import org.example.infrastructure.ApplicationContext;
import org.example.infrastructure.annotation.Env;
import org.example.infrastructure.annotation.Inject;
import org.example.infrastructure.annotation.PostConstruct;
import org.example.infrastructure.annotation.Qualifier;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EvnAnnotationObjectConfigurator  implements ObjectConfigurator {
    @Override
    public void configure(Object obj, ApplicationContext context) throws IllegalAccessException, InvocationTargetException {
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(Env.class)) {
                String value = field.getAnnotation(Env.class).value();
                if (!value.isEmpty()) {
                    field.setAccessible(true);
                    field.set(obj, value);
                } else {
                    field.setAccessible(true);
                    field.set(obj, field.getName());
                }
            }
        }

    }
}
