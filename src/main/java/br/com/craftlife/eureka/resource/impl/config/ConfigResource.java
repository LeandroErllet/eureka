package br.com.craftlife.eureka.resource.impl.config;

import br.com.craftlife.eureka.module.EurekaModule;
import br.com.craftlife.eureka.resource.FileResource;
import br.com.craftlife.eureka.resource.impl.yaml.YamlResourceSection;

public class ConfigResource extends YamlResourceSection implements FileResource {

    private final EurekaModule module;

    public ConfigResource(EurekaModule module) {
        super(module.getConfig(), "");
        this.module = module;
    }

    @Override
    public boolean reload() {
        try {
            module.reloadConfig();
            return true;
        } catch (Exception exc) {
            return false;
        }
    }

    @Override
    public boolean save() {
        try {
            module.saveConfig();
            return true;
        } catch (Exception exc) {
            return false;
        }
    }
}
