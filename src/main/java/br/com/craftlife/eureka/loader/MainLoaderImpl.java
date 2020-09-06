package br.com.craftlife.eureka.loader;

import br.com.craftlife.eureka.database.DatabaseManager;
import br.com.craftlife.eureka.injector.EurekaInjectorModule;
import br.com.craftlife.eureka.loader.types.EurekaLoader;
import br.com.craftlife.eureka.module.EurekaModule;
import br.com.craftlife.eureka.resource.ResourceProvider;
import br.com.craftlife.eureka.server.ServerManager;
import br.com.craftlife.eureka.server.internal.ServerManagerImpl;
import co.aikar.commands.PaperCommandManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainLoaderImpl extends AbstractLoader {

    @Getter
    private final EurekaModule module;

    @Getter
    private final ExecutorService executorService;

    @Getter
    private final ServerManager serverManager;

    @Getter
    private final String moduleName;

    @Getter
    private final DatabaseManager databaseManager;

    @Getter
    private final ResourceProvider resourceProvider;

    @Getter(AccessLevel.PROTECTED)
    private Injector injector;

    @Getter
    private PaperCommandManager commandManager;

    @Getter
    private final List<ChildLoaderImpl> childLoaders = new ArrayList<>();

    @Getter
    private final EurekaInjectorModule injectorModule;

    MainLoaderImpl(EurekaModule module) {
        this.module = module;
        this.moduleName = module.getName();
        this.executorService = Executors.newFixedThreadPool(5);
        this.databaseManager = new DatabaseManager();
        this.resourceProvider = new ResourceProvider(module);
        this.serverManager = new ServerManagerImpl(this);
        this.injectorModule = new EurekaInjectorModule(this);
        this.injector = Guice.createInjector(injectorModule);
    }

    @Override
    public void load() {
        super.load();
    }

    @Override
    public void enable() {
        this.commandManager = new PaperCommandManager(module);
        super.enable();
        databaseManager.enable();
        ((ServerManagerImpl) serverManager).init();
    }

    @Override
    public void unload() {
        databaseManager.unload();
        try {
            executorService.shutdown();
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public EurekaLoader getParent() {
        return null;
    }

    public void addChildLoader(ChildLoaderImpl loader) {
        childLoaders.add(loader);
    }

}
