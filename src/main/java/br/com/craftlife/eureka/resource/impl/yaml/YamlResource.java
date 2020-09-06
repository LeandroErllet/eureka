package br.com.craftlife.eureka.resource.impl.yaml;

import br.com.craftlife.eureka.resource.FileResource;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class YamlResource extends YamlResourceSection implements FileResource {

    private final YamlConfiguration yamlConfiguration;

    private final File file;

    public YamlResource(YamlConfiguration yamlConfiguration, File file) {
        super(yamlConfiguration, "");
        this.yamlConfiguration = yamlConfiguration;
        this.file = file;
    }

    @Override
    public boolean reload() {
        try {
            yamlConfiguration.load(file);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean save() {
        try {
            yamlConfiguration.save(file);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
