package org.example.infrastructure;

import lombok.Getter;
import lombok.Setter;
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
        T object = objectFactory.createObject(implClass);

        if (objectConfigReader.isPrototype(implClass)) {
            return  object;
        }

        if (singletonCache.containsKey(implClass)) {
            return (T) singletonCache.get(implClass);
        }

        singletonCache.put(implClass, object);
        return object;
    }
}
