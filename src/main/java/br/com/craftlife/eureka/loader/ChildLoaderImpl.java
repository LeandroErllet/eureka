package br.com.craftlife.eureka.loader;

import br.com.craftlife.eureka.database.DatabaseManager;
import br.com.craftlife.eureka.injector.EurekaInjectorModule;
import br.com.craftlife.eureka.module.EurekaModule;
import br.com.craftlife.eureka.resource.ResourceProvider;
import br.com.craftlife.eureka.server.ServerManager;
import co.aikar.commands.PaperCommandManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.concurrent.ExecutorService;

public class ChildLoaderImpl extends AbstractLoader {

    @Getter
    private final MainLoaderImpl parent;

    @Getter
    private final EurekaModule module;

    @Getter
    private final String moduleName;

    @Getter
    private final ResourceProvider resourceProvider;

    @Getter(AccessLevel.PROTECTED)
    private final Injector injector;

    @Getter
    private final PaperCommandManager commandManager;

    ChildLoaderImpl(MainLoaderImpl parent, EurekaModule module) {
        this.parent = parent;
        this.module = module;
        this.moduleName = module.getName();
        this.commandManager = new PaperCommandManager(module);
        this.resourceProvider = new ResourceProvider(module);
        EurekaInjectorModule injectorModule = new EurekaInjectorModule(this);
        this.injector = Guice.createInjector(injectorModule);
    }

    @Override
    public ExecutorService getExecutorService() {
        return parent.getExecutorService();
    }

    @Override
    public DatabaseManager getDatabaseManager() {
        return parent.getDatabaseManager();
    }

    @Override
    public ServerManager getServerManager() {
        return parent.getServerManager();
    }

    @Override
    public void unload() {

    }
}
