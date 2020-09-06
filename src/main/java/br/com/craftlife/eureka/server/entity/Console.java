package br.com.craftlife.eureka.server.entity;

import br.com.craftlife.eureka.server.ServerManager;
import br.com.craftlife.eureka.server.entity.internal.SenderImpl;
import org.bukkit.command.ConsoleCommandSender;

public class Console extends SenderImpl {

    private final ConsoleCommandSender console;

    private final ServerManager serverManager;

    public Console(ConsoleCommandSender console, ServerManager serverManager) {
        super(console, serverManager);
        this.console = console;
        this.serverManager = serverManager;
    }

    @Override
    public boolean isPlayer() {
        return false;
    }
}
