package org.example.infrastructure.configreader;

import org.example.infrastructure.annotation.Component;
import org.example.infrastructure.annotation.Qualifier;
import org.reflections.Reflections;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class JavaObjectConfigReader implements ObjectConfigReader {

    private Reflections reflections;

    public JavaObjectConfigReader(String packageToScan) {
        this.reflections = new Reflections(packageToScan);
    }

    @Override
    public <T> Class<? extends T> getImplClass(Class<T> cls) {
        if (!cls.isInterface()) {
            if (!cls.isAnnotationPresent(Component.class)) {
                throw new RuntimeException("Class is not annotated with @Component: " + cls.getName());
            }
            return cls;
        }

        Set<Class<? extends T>> subTypesOf =
                reflections.getSubTypesOf(cls);

        if (subTypesOf.size() != 1) {
            throw new RuntimeException("Class is not annotated with @Qualifier: " + cls.getName());
        }

        Class<? extends T> implClass = subTypesOf.iterator().next();
        if(!implClass.isAnnotationPresent(Component.class)) {
            throw new RuntimeException("Implementation class must be annotated with @Component: " + implClass.getName());
        }

        return implClass;
    }

    @Override
    public <T> Collection<Class<? extends T>> getImplClasses(Class<T> cls) {
        return reflections.getSubTypesOf(cls);
    }
}
