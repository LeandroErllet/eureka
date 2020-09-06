package br.com.craftlife.eureka.loader;

import br.com.craftlife.eureka.loader.types.EurekaLoader;
import br.com.craftlife.eureka.scanner.ClassScanner;
import co.aikar.commands.BaseCommand;
import com.google.inject.Injector;
import org.bukkit.event.Listener;

import java.util.ArrayList;
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
        getResourceProvider().init();
        ClassScanner scanner = new ClassScanner(getModule().getClass(), getModule().getClass().getClassLoader());
        scanner.scan();
        scanner.getEntities().forEach(getDatabaseManager()::registerClass);

        getInjector().injectMembers(getModule());
    }

    @Override
    public void enable() {
        getDatabaseManager().migrate(this);
        listeners.forEach(listener ->
                getModule().getServer().getPluginManager().registerEvents(listener, getModule())
        );
        commands.forEach(commands -> getCommandManager().registerCommand(commands));
        commands.clear();
        listeners.clear();
    }

}
