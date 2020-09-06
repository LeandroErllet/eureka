package br.com.craftlife.eureka.injector.config;

import br.com.craftlife.eureka.resource.FileResource;
import br.com.craftlife.eureka.resource.ResourceProvider;
import br.com.craftlife.eureka.util.ClassUtils;
import com.google.inject.MembersInjector;

import java.io.IOException;
import java.lang.reflect.Field;

public class InstanceConfigMembersInjector<T> implements MembersInjector<T> {

    private final Field field;

    private final ResourceProvider resourceProvider;

    private ConfigInstance<?> configInstance;


    public InstanceConfigMembersInjector(Field field, ResourceProvider resourceProvider) {
        this.field = field;
        this.resourceProvider = resourceProvider;
        initResource();
    }

    private void initResource() {
        field.setAccessible(true);
        Class<T> genericType = ClassUtils.getGenericType(field);
        Config config = field.getAnnotation(Config.class);
        try {
            FileResource resource = resourceProvider.loadResource(config.resource());
            configInstance = new ConfigInstance<>(resource, config.value(), genericType);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void injectMembers(T instance) {
        try {
            field.set(instance, configInstance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
