package org.example.infrastructure.configurator;

import lombok.SneakyThrows;
import org.example.infrastructure.ApplicationContext;
import org.example.infrastructure.annotation.*;

import java.lang.reflect.Field;


public class InjectAnnotationObjectConfigurator implements ObjectConfigurator {

    @Override
    @SneakyThrows
    public void configure(Object obj, ApplicationContext context) {
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(Inject.class)) {
                field.setAccessible(true);
                Qualifier qualifier = field.getAnnotation(Qualifier.class);
                Object value;
                if (qualifier != null) {
                    value = context.getObject(qualifier.value());
                } else {
                    value = context.getObject(field.getType());
                }
                field.set(obj, value);
            }
        }
    }
}
