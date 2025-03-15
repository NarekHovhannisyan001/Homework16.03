package org.example.infrastructure.configreader;

import org.example.app.exception.ComponentNotFoundException;
import org.example.app.exception.MultipleImplementationsException;
import org.example.app.exception.NoImplementationFoundException;
import org.example.infrastructure.annotation.Component;
import org.example.infrastructure.annotation.Qualifier;
import org.example.infrastructure.annotation.Scope;
import org.example.infrastructure.annotation.ScopeType;
import org.reflections.Reflections;

import javax.naming.CommunicationException;
import java.util.*;
import java.util.stream.Collectors;

public class JavaObjectConfigReader implements ObjectConfigReader {

    private Reflections reflections;

    public JavaObjectConfigReader(String packageToScan) {
        this.reflections = new Reflections(packageToScan);
    }

    @Override
    public <T> Class<? extends T> getImplClass(Class<T> cls) {
        if (!cls.isInterface()) {
            if (!cls.isAnnotationPresent(Component.class)) {
                throw new ComponentNotFoundException("Class is not annotated with @Component: " + cls.getName());
            }
            return cls;
        }

        Set<Class<? extends T>> subTypesOf =
                reflections.getSubTypesOf(cls);
        List<Class<? extends T>> subTypes = subTypesOf.stream().filter(t -> !t.isAnnotationPresent(Component.class)).collect(Collectors.toList());

        if (subTypes.isEmpty()) {
            throw new NoImplementationFoundException("No @Component implementation found for: " + cls.getName());
        }
        if (subTypes.size() > 1) {
            throw new MultipleImplementationsException("Multiple @Component implementations found for: " + cls.getName());
        }

        return subTypes.get(0);
    }

    @Override
    public <T> Collection<Class<? extends T>> getImplClasses(Class<T> cls) {
        return reflections.getSubTypesOf(cls);
    }

    public boolean isPrototype(Class<?> cls) {
        return cls.isAnnotationPresent(Scope.class) && cls.getDeclaredAnnotation(Scope.class).value().equals(ScopeType.PROTOTYPE);
    }

    public  boolean isSingleton(Class<?> cls) {
       if (!cls.isAnnotationPresent(Scope.class)) {
           return true;
       }
       return cls.getDeclaredAnnotation(Scope.class).value().equals(ScopeType.SINGLETON);
    }
}
