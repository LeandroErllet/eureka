package br.com.craftlife.eureka.resource.impl.yaml;

import br.com.craftlife.eureka.resource.ResourceSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Optional;
import java.util.Set;

public class YamlResourceSection implements ResourceSection {

    private final FileConfiguration yamlConfiguration;

    private final String basePath;

    public YamlResourceSection(FileConfiguration yamlConfiguration, String basePath) {
        this.yamlConfiguration = yamlConfiguration;
        this.basePath = basePath;
    }

    private String resolvePath(String path) {
        if (basePath.isEmpty()) {
            return path;
        }
        return basePath + "." + path;
    }

    @Override
    public Optional<Object> get(String path) {
        if (path == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(yamlConfiguration.get(resolvePath(path)));
    }

    @Override
    public <T> Optional<T> get(String path, Class<T> type) {
        return get(path).flatMap(value -> {
            try {
                return Optional.of(type.cast(value));
            } catch (Exception exc) {
                return Optional.empty();
            }
        });
    }

    @Override
    public Optional<String> getString(String path) {
        return get(path, String.class);
    }

    @Override
    public Optional<Double> getDouble(String path) {
        return get(path, Double.class);
    }

    @Override
    public Optional<Integer> getInt(String path) {
        return get(path, Integer.class);
    }

    @Override
    public void set(String path, Object value) {
        yamlConfiguration.set(resolvePath(path), value);
    }

    @Override
    public ResourceSection getSection(String path) {
        return new YamlResourceSection(yamlConfiguration, resolvePath(path));
    }

    @Override
    public Set<String> getKeys() {
        return yamlConfiguration.getConfigurationSection(basePath).getKeys(false);
    }

}
