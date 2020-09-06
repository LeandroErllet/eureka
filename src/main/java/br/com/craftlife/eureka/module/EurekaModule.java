package br.com.craftlife.eureka.module;

import br.com.craftlife.eureka.database.DatabaseConfiguration;
import br.com.craftlife.eureka.injector.resource.InjectResource;
import br.com.craftlife.eureka.loader.LoaderProvider;
import br.com.craftlife.eureka.loader.types.EurekaLoader;
import br.com.craftlife.eureka.resource.FileResource;
import br.com.craftlife.eureka.resource.ResourceSection;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public abstract class EurekaModule extends JavaPlugin {

    @InjectResource("database.yml")
    private FileResource databaseConfig;

    @Getter
    @InjectResource("config.yml")
    private FileResource configuration;

    private EurekaLoader loader;

    public EurekaModule() {
        loader = LoaderProvider.forModule(this);
    }

    @Override
    public final void onLoad() {
        super.onLoad();
        loader.load();
        this.configure();
    }

    @Override
    public final void onEnable() {
        super.onEnable();
        loader.enable();
        this.init();
    }

    @Override
    public final void onDisable() {
        super.onDisable();
        if (loader != null) {
            loader.unload();
            loader = null;
        }
        this.finalize();
    }

    public void configure() {
    }

    protected final boolean easySetup() {
        FileResource config = Optional.ofNullable(databaseConfig).orElse(configuration);
        if (config != null) {
            DatabaseConfiguration dbconfig = new DatabaseConfiguration();
            ResourceSection section = config.getSection("database");

            dbconfig.setUser(section.getString("user").orElse(null));
            dbconfig.setPassword(section.getString("password").orElse(null));
            dbconfig.setUrl(section.getString("url").orElse(null));
            section.getString("dialect").ifPresent(dbconfig::setDialect);
            section.getString("driver").ifPresent(dbconfig::setDriver);
            section.getString("redis_url").ifPresent(dbconfig::setRedisUrl);

            loader.getDatabaseManager().setDatabaseConfiguration(dbconfig);
        }
        return false;
    }

    public abstract void init();
    
    public void finalize() {
	}

}
