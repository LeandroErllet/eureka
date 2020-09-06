package br.com.craftlife.eureka.util;

import br.com.craftlife.eureka.server.permission.NamedPermission;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ClassUtils {

    public static Optional<Class> getGenericType(Class<?> clazz) {
        List<Class> types = getAllGenericTypes(clazz);
        Class generic = types.stream().findFirst().orElseGet(() -> {
            if (clazz.getSuperclass() != null) {
                return getGenericType(clazz.getSuperclass()).get();
            }
            return null;
        });
        return Optional.ofNullable(generic);
    }

    public static <T> Class<T> getGenericType(Field field) {
        return (Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
    }

    public static List<Class> getAllGenericTypes(Class<?> clazz) {
        Type annotatedType = clazz.getAnnotatedSuperclass().getType();
        if (annotatedType instanceof ParameterizedType) {
            return Arrays.stream(((ParameterizedType) annotatedType).getActualTypeArguments())
                    .filter(type -> type instanceof Class)
                    .map(type -> (Class) type)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public static String getPermissionFromClass(Class<?> clazz) {
        if (clazz.isAnnotationPresent(NamedPermission.class)) {
            NamedPermission namedPermission = clazz.getAnnotation(NamedPermission.class);
            return namedPermission.value();
        }
        String name = clazz.getSimpleName().toLowerCase();
        if (name.endsWith("ed")) {
            return name.substring(0, name.length() - 2);
        }
        return name;
    }

}
