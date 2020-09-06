package br.com.craftlife.eureka.resource;

import br.com.craftlife.eureka.module.EurekaModule;
import br.com.craftlife.eureka.resource.impl.config.ConfigResource;
import br.com.craftlife.eureka.resource.impl.yaml.YamlResource;
import br.com.craftlife.eureka.resource.messages.MessageProvider;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResourceProvider {

    private final HashMap<String, FileResource> resources = new HashMap<>();

    private final EurekaModule module;

    @Getter
    private final FileResource configResource;

    @Getter
    private MessageProvider messageProvider;

    public ResourceProvider(EurekaModule module) {
        this.module = module;
        this.configResource = new ConfigResource(module);
        init();
    }

    public void init() {
        try {
            this.messageProvider = new MessageProvider(loadResource("messages.yml"));
        } catch (Exception e) {
            this.messageProvider = new MessageProvider(null);
        }
    }

    public FileResource loadResource(String name) throws IOException {
        if (name.equalsIgnoreCase("config.yml")) {
            return configResource;
        }
        if (resources.containsKey(name)) {
            return resources.get(name);
        }
        module.getDataFolder().mkdirs();
        File file = new File(module.getDataFolder(), name);
        if (!file.exists()) {
            InputStream resource = module.getResource(name);
            if (resource == null) {
                return null;
            }
            Files.copy(resource, file.toPath());
        }
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        FileResource resource = new YamlResource(yamlConfiguration, file);
        resources.put(name, resource);
        return resource;
    }

    public Set<FileResource> getLoadedResources() {
        return Stream.concat(resources.values().stream(), Stream.of(configResource))
                .collect(Collectors.toSet());
    }

}
