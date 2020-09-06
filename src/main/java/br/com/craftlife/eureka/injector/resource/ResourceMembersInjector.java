package br.com.craftlife.eureka.injector.resource;

import br.com.craftlife.eureka.resource.FileResource;
import br.com.craftlife.eureka.resource.ResourceProvider;
import com.google.inject.MembersInjector;

import java.io.IOException;
import java.lang.reflect.Field;

class ResourceMembersInjector<T> implements MembersInjector<T> {

    private final Field field;

    private final ResourceProvider resourceProvider;

    private FileResource fileResource;

    private final InjectResource injectResource;

    public ResourceMembersInjector(Field field, ResourceProvider resourceProvider) {
        this.field = field;
        this.resourceProvider = resourceProvider;
        this.injectResource = field.getAnnotation(InjectResource.class);
        initResource();
    }

    private void initResource() {
        field.setAccessible(true);
        try {
            fileResource = resourceProvider.loadResource(injectResource.value());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void injectMembers(T instance) {
        try {
            field.set(instance, fileResource);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
