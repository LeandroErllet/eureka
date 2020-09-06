package br.com.craftlife.eureka.loader;

import br.com.craftlife.eureka.loader.types.EurekaLoader;
import br.com.craftlife.eureka.loader.types.WithParent;
import br.com.craftlife.eureka.module.EurekaModule;

import java.util.HashMap;

public class LoaderProvider {

    private static final HashMap<Class<?>, MainLoaderImpl> loaders = new HashMap<>();

    public static EurekaLoader forModule(EurekaModule module) {
        Class<?> moduleClass = module.getClass();
        if (moduleClass.isAnnotationPresent(WithParent.class)) {
            WithParent withParent = moduleClass.getAnnotation(WithParent.class);
            MainLoaderImpl loader = loaders.get(withParent.value());
            if (loader == null) {
                throw new IllegalArgumentException("The parent loader could not be found");
            }
            ChildLoaderImpl childLoader = new ChildLoaderImpl(loader, module);
            loader.addChildLoader(childLoader);
            return childLoader;
        }
        MainLoaderImpl loader = new MainLoaderImpl(module);
        loaders.put(moduleClass, loader);
        return loader;
    }

}
