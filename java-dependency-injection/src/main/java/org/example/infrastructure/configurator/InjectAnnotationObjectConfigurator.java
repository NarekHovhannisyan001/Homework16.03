package org.example.infrastructure.configurator;

import lombok.SneakyThrows;
import org.example.infrastructure.ApplicationContext;
import org.example.infrastructure.annotation.*;

import javax.crypto.spec.PSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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
            } else if (field.isAnnotationPresent(Property.class)) {
               try(FileInputStream fileInputStream = new FileInputStream("src/main/java/org/example/resources/application.properties.properties" )) {
                Properties properties = new Properties();
                properties.load(fileInputStream);
                String value =  field.getAnnotation(Property.class).value();
                if (!value.isEmpty()) {
                    field.setAccessible(true);
                    field.set(obj,properties.getProperty(value));
                } else {
                    field.setAccessible(true);
                    field.set(obj,field.getName());
                }
               } catch (FileNotFoundException e) {
                   System.err.println(e.getMessage() + " file not found");
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
