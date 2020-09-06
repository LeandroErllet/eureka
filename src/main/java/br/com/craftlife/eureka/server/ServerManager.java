package br.com.craftlife.eureka.server;

import br.com.craftlife.eureka.resource.messages.Message;
import br.com.craftlife.eureka.server.entity.Console;
import br.com.craftlife.eureka.server.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Delegate;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public abstract class ServerManager {

    private interface Delegated {
        List<World> getWorlds();

        World getWorld(String x);

        World createWorld(WorldCreator x);

        void shutdown();
    }

    @Delegate(types = Delegated.class)
    @Setter(AccessLevel.PROTECTED)
    @Getter(AccessLevel.PROTECTED)
    private Server server;

    public abstract Optional<User> getUser(String name);

    public abstract Optional<User> getUser(Player player);

    public abstract Optional<User> getUser(UUID id);

    public abstract Console getConsole();

    public abstract Set<User> matchUser(String name);

    public abstract Set<User> getOnlineUsers();

    public abstract void broadcastMessage(Message message);

}
