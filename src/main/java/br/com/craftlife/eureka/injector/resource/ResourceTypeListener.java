package br.com.craftlife.eureka.injector.resource;

import br.com.craftlife.eureka.injector.config.Config;
import br.com.craftlife.eureka.injector.config.ConfigInstance;
import br.com.craftlife.eureka.injector.config.InstanceConfigMembersInjector;
import br.com.craftlife.eureka.injector.config.StaticConfigMembersInjector;
import br.com.craftlife.eureka.loader.types.EurekaLoader;
import com.google.inject.MembersInjector;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

import java.lang.reflect.Field;
import java.util.Arrays;

public class ResourceTypeListener implements TypeListener {

    private final EurekaLoader loader;

    public ResourceTypeListener(EurekaLoader loader) {
        this.loader = loader;
    }

    @Override
    public <T> void hear(TypeLiteral<T> typeLiteral, TypeEncounter<T> typeEncounter) {
        Class<?> clazz = typeLiteral.getRawType();
        while (clazz != null) {
            Arrays.stream(clazz.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(Config.class) ||
                            field.isAnnotationPresent(InjectResource.class) ||
                            field.isAnnotationPresent(InjectMessage.class))
                    .map(this::getMembersInjector)
                    .forEach(typeEncounter::register);
            clazz = clazz.getSuperclass();
        }
    }

    private <T> MembersInjector<T> getMembersInjector(Field field) {
        if (field.isAnnotationPresent(InjectMessage.class)) {
            return new MessageMembersInjector<>(field, loader.getResourceProvider().getMessageProvider());
        } else if (field.isAnnotationPresent(InjectResource.class)) {
            return new ResourceMembersInjector<>(field, loader.getResourceProvider());
        } else if (field.getType().equals(ConfigInstance.class)) {
            return new InstanceConfigMembersInjector<>(field, loader.getResourceProvider());
        } else {
            return new StaticConfigMembersInjector<>(field, loader.getResourceProvider());
        }
    }
}
