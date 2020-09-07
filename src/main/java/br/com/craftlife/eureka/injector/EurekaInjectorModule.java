package br.com.craftlife.eureka.injector;

import br.com.craftlife.eureka.database.DatabaseManager;
import br.com.craftlife.eureka.database.EurekaBD;
import br.com.craftlife.eureka.database.memory.IMemoryStorage;
import br.com.craftlife.eureka.injector.resource.ResourceTypeListener;
import br.com.craftlife.eureka.injector.interceptor.TransactionalInterceptor;
import br.com.craftlife.eureka.injector.interceptor.async.Async;
import br.com.craftlife.eureka.injector.interceptor.async.AsyncInterceptor;
import br.com.craftlife.eureka.loader.types.EurekaLoader;
import br.com.craftlife.eureka.module.EurekaModule;
import br.com.craftlife.eureka.resource.messages.MessageProvider;
import br.com.craftlife.eureka.server.ServerManager;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.matcher.Matchers;

import javax.transaction.Transactional;

public class EurekaInjectorModule implements Module {

    private final EurekaLoader loader;

    public EurekaInjectorModule(EurekaLoader loader) {
        this.loader = loader;
    }

    @Override
    public void configure(Binder binder) {
        binder.bind((Class) loader.getModule().getClass()).toInstance(loader.getModule());
        binder.bindListener(Matchers.any(), new ResourceTypeListener(loader));
        binder.bindInterceptor(Matchers.subclassesOf(EurekaBD.class), Matchers.annotatedWith(Transactional.class),
                new TransactionalInterceptor(loader.getDatabaseManager()));
        binder.bindInterceptor(Matchers.any(), Matchers.annotatedWith(Async.class),
                new AsyncInterceptor(loader.getExecutorService()));
    }

    @Provides
    DatabaseManager provideDatabaseManager() {
        return loader.getDatabaseManager();
    }

    @Provides
    EurekaLoader provideEurekaLoader() {
        return loader;
    }

    @Provides
    EurekaModule provideEurekaModule() {
        return loader.getModule();
    }

    @Provides
    MessageProvider provideMessageProvider() {
        return loader.getResourceProvider().getMessageProvider();
    }

    @Provides
    ServerManager provideServerManager() {
        return loader.getServerManager();
    }

    @Provides
    IMemoryStorage provideMemoryStorage() {
        return loader.getDatabaseManager().getMemoryStorage();
    }

}
