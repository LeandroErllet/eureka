package br.com.craftlife.eureka.loader;

import br.com.craftlife.eureka.loader.types.EurekaLoader;
import br.com.craftlife.eureka.loader.types.Module;
import br.com.craftlife.eureka.scanner.ClassScanner;
import co.aikar.commands.BaseCommand;
import com.google.inject.Injector;
import lombok.val;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractLoader implements EurekaLoader {

    private final List<Listener> listeners = new ArrayList<>();

    private final List<BaseCommand> commands = new ArrayList<>();

    protected abstract Injector getInjector();

    @Override
    public void registerEvents(Listener listener) {
        if (getModule().isEnabled()) {
            getModule().getServer().getPluginManager().registerEvents(listener, getModule());
        } else {
            listeners.add(listener);
        }
    }

    @Override
    public void registerCommands(BaseCommand listener) {
        if (getModule().isEnabled()) {
            getCommandManager().registerCommand(listener);
        } else {
            commands.add(listener);
        }
    }

    @Override
    public void load() {
        loadModules();
        getResourceProvider().init();
        getInjector().injectMembers(getModule());
    }

    @Override
    public void enable() {
        getDatabaseManager().migrate(this);
        ClassScanner scanner = new ClassScanner(getModule().getClass(), getModule().getClass().getClassLoader());
        scanner.scan();
        scanner.getEntities().forEach(getDatabaseManager()::registerClass);
        scanner.getListeners()
                .stream()
                .filter(Listener.class::isAssignableFrom)
                .map(aClass -> getInjector().getInstance(aClass))
                .forEach(aClass -> getModule().getServer().getPluginManager().registerEvents((Listener) aClass, getModule()));
        scanner.getCommands().forEach(aClass -> getCommandManager().registerCommand(getInjector().getInstance(aClass)));
    }


    private Collection<Class<?>> loadModules() {
        val moduleLoader = new ModuleLoader(getModule().getClass().getClassLoader());
        val moduleFolder = new File(getModule().getDataFolder(), "modules");
        moduleFolder.mkdirs();
        return moduleLoader.loadModules("br.com.craftlife", moduleFolder);
    }

}
