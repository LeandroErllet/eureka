package br.com.craftlife.eureka.loader;

import br.com.craftlife.eureka.loader.types.EurekaLoader;
import br.com.craftlife.eureka.module.EurekaModule;

import java.util.HashMap;

public class LoaderProvider {

    private static final HashMap<Class<?>, MainLoaderImpl> loaders = new HashMap<>();

    public static EurekaLoader forModule(EurekaModule module) {
        Class<?> moduleClass = module.getClass();
        MainLoaderImpl loader = new MainLoaderImpl(module);
        loaders.put(moduleClass, loader);
        return loader;
    }

}
