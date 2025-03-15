package org.example.infrastructure.proxywrapper;

import org.example.infrastructure.annotation.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

public   class LoggingInvocationHandler implements InvocationHandler {
    private final Object target;
    private final Class<?> targetClass;

    public LoggingInvocationHandler(Object target, Class<?> cls) {
        this.target = target;
        this.targetClass = cls;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!(targetClass.isAnnotationPresent(Log.class) &&
                !targetClass.getDeclaredMethod(method.getName(), method.getParameterTypes()).isAnnotationPresent(Log.class))) {
            return method.invoke(target, args);
        }

            System.out.printf(
                    "[LOG] Method called: %s | Arguments: %s\n",
                    method.getName(),
                    Arrays.toString(args)
            );
        return method.invoke(target, args);
    }


}
