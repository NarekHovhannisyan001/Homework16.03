package org.example.infrastructure.configreader;

import lombok.extern.apachecommons.CommonsLog;
import lombok.var;
import org.reflections.Reflections;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class JavaObjectConfigReader implements ObjectConfigReader {

    private Reflections reflections;

    public JavaObjectConfigReader(String packageToScan) {
        this.reflections = new Reflections(packageToScan);
    }

    @Override
    public <T> Class<? extends T> getImplClass(Class<T> cls) {
        if (!cls.isInterface()) {
            if (cls.isAnnotationPresent((CommonsLog.class))) {
                return cls;
            }
            throw new RuntimeException("Class " + cls.getName() + " is not a @Component");
        }

        Set<Class<? extends T>> subTypesOf =
                reflections.getSubTypesOf(cls);
        Set<Class<?>> componentCount = subTypesOf.stream().filter((subType -> subType.isAnnotationPresent((CommonsLog.class)))).collect(Collectors.toSet());

//      this is addtional if componentCount is x so this is correct
//        if (subTypesOf.size() != 1) {
//            throw new RuntimeException("Interface should have only one implementation");
//        }
        if (componentCount.size() != 1) {
            throw new RuntimeException("Class " + cls.getName() + " must have to one @Componetns but found " + componentCount.size());
        }

        return (Class<? extends T>) componentCount.iterator().next();
    }

    @Override
    public <T> Collection<Class<? extends T>> getImplClasses(Class<T> cls) {
        return reflections.getSubTypesOf(cls).stream().filter(subType -> subType.isAnnotationPresent((CommonsLog.class))).collect(Collectors.toSet());
    }
}
