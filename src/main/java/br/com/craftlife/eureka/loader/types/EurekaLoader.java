package br.com.craftlife.eureka.loader.types;

import br.com.craftlife.eureka.database.DatabaseManager;
import br.com.craftlife.eureka.module.EurekaModule;
import br.com.craftlife.eureka.resource.ResourceProvider;
import br.com.craftlife.eureka.server.ServerManager;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import org.bukkit.event.Listener;

import java.util.concurrent.ExecutorService;

public interface EurekaLoader {

    EurekaLoader getParent();

    EurekaModule getModule();

    ExecutorService getExecutorService();

    String getModuleName();

    DatabaseManager getDatabaseManager();

    ResourceProvider getResourceProvider();

    ServerManager getServerManager();

    PaperCommandManager getCommandManager();

    void registerEvents(Listener listener);

    void registerCommands(BaseCommand listener);

    void load();

    void enable();

    void unload();

}
