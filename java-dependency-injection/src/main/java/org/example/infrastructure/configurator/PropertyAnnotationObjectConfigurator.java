package org.example.infrastructure.configurator;

import org.example.infrastructure.ApplicationContext;
import org.example.infrastructure.annotation.Property;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public class PropertyAnnotationObjectConfigurator  implements ObjectConfigurator {
    @Override
    public void configure(Object obj, ApplicationContext context) throws IllegalAccessException, InvocationTargetException, IOException {
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(Property.class)) {
                try (FileInputStream fileInputStream = new FileInputStream("src/main/java/org/example/resources/application.properties.properties")) {
                    Properties properties = new Properties();
                    properties.load(fileInputStream);
                    String value = field.getAnnotation(Property.class).value();
                    if (!value.isEmpty()) {
                        field.setAccessible(true);
                        field.set(obj, properties.getProperty(value));
                    } else {
                        field.setAccessible(true);
                        field.set(obj, field.getName());
                    }
                } catch (FileNotFoundException e) {
                    System.err.println(e.getMessage() + " file not found");
                }
            }
        }
    }
}
