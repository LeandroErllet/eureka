package br.com.craftlife.eureka.injector.config;

import br.com.craftlife.eureka.resource.FileResource;
import br.com.craftlife.eureka.resource.ResourceProvider;
import com.google.inject.MembersInjector;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Optional;

public class StaticConfigMembersInjector<T> implements MembersInjector<T> {

    private final Field field;

    private final ResourceProvider resourceProvider;

    private FileResource fileResource;

    private final Config config;

    public StaticConfigMembersInjector(Field field, ResourceProvider resourceProvider) {
        this.field = field;
        this.resourceProvider = resourceProvider;
        this.config = field.getAnnotation(Config.class);
        initResource();
    }

    private void initResource() {
        field.setAccessible(true);
        try {
            fileResource = resourceProvider.loadResource(config.resource());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void injectMembers(T instance) {
        try {
            Optional<?> value = fileResource.get(config.value(), field.getType());
            if (value.isPresent()) {
                field.set(instance, value.get());
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
