package org.example.infrastructure.proxywrapper;

import org.example.infrastructure.annotation.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

public   class LoggingInvocationHandler implements InvocationHandler {
    private final Object target;
    private final boolean classHasLogAnnotation;

    public LoggingInvocationHandler(Object target, boolean classHasLogAnnotation) {
        this.target = target;
        this.classHasLogAnnotation = classHasLogAnnotation;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (classHasLogAnnotation || method.isAnnotationPresent(Log.class)) {
            System.out.printf(
                    "[LOG] Method called: %s | Arguments: %s\n",
                    method.getName(),
                    Arrays.toString(args)
            );
        }
        return method.invoke(target, args);
    }
}
