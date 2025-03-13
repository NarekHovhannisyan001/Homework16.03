package org.example.infrastructure.configurator;

import lombok.SneakyThrows;
import org.example.infrastructure.ApplicationContext;
import org.example.infrastructure.annotation.*;

import javax.crypto.spec.PSource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
                }
                else {
                    value = context.getObject(field.getType());
                }
                field.set(obj, value);
            } else if (field.isAnnotationPresent(Env.class)) {
                String value = field.getAnnotation(Env.class).value();
                if (!value.isEmpty()) {
                    field.setAccessible(true);
                    field.set(obj,value);
                } else {
                    field.setAccessible(true);
                    field.set(obj,field.getName());
                }
            }
        }
        for (Method method : obj.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(PostConstruct.class) && method.getParameterTypes().length == 0) {
                method.setAccessible(true);
                method.invoke(obj);
            }
        }
    }
}
