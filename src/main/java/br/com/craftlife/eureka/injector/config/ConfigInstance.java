package br.com.craftlife.eureka.injector.config;

import br.com.craftlife.eureka.resource.FileResource;

import java.util.Optional;

public class ConfigInstance<T> {

    private final FileResource resource;

    private final String key;

    private final Class<T> type;

    ConfigInstance(FileResource resource, String key, Class<T> type) {
        this.resource = resource;
        this.key = key;
        this.type = type;
    }

    public Optional<T> getValue() {
        return resource.get(key, type);
    }

    public void setValue(T value) {
        resource.set(key, value);
    }

    public boolean save() {
        return resource.save();
    }

    public boolean reload() {
        return resource.reload();
    }

}
