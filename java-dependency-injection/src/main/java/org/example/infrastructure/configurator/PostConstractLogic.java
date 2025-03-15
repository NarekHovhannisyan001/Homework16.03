package org.example.infrastructure.configurator;

import org.example.app.exception.PostConstractException;
import org.example.infrastructure.annotation.PostConstruct;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PostConstractLogic {
    public PostConstractLogic(Object obj) throws InvocationTargetException, IllegalAccessException {
        for (Method method : obj.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(PostConstruct.class)) {
                if (method.getParameterTypes().length != 0) {
                    throw new PostConstractException("@PostConstruct method must have exactly one parameter");
                }
                method.setAccessible(true);
                method.invoke(obj);
            }
        }
    }
}


