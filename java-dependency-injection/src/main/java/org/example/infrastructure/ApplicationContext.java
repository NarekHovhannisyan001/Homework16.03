package org.example.infrastructure;

import lombok.Getter;
import lombok.Setter;
import org.example.app.exception.QualifierTypeMismatchException;
import org.example.infrastructure.annotation.Component;
import org.example.infrastructure.annotation.Scope;
import org.example.infrastructure.annotation.ScopeType;
import org.example.infrastructure.configreader.ObjectConfigReader;

import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {

    @Setter
    private ObjectFactory objectFactory;

    @Getter
    private ObjectConfigReader objectConfigReader;

    private Map<Class<?>, Object> singletonCache = new HashMap<>();

    public ApplicationContext(ObjectConfigReader objectConfigReader) {
        this.objectConfigReader = objectConfigReader;
    }

    @SuppressWarnings("unchecked")
    public <T> T getObject(Class<T> cls) {
        Class<? extends T> implClass = objectConfigReader.getImplClass(cls);

        if (singletonCache.containsKey(implClass)) {
            return (T) singletonCache.get(implClass);
        }
        T object = objectFactory.createObject(implClass);

        if (objectConfigReader.isSingleton(implClass)) {
            singletonCache.put(implClass, object);
        }

        return object;
    }

    public <T> T
    getObject(Class<T> cls, Class<?> expectedType) {
        if (!expectedType.isAssignableFrom(cls)) {
            throw new QualifierTypeMismatchException("Error: Expected type " + expectedType.getName() +
                    " but got incompatible type " + cls.getName()
            );
        }

        return getObject(cls);
    }
}
