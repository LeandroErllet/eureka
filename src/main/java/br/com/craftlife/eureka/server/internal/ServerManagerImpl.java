package br.com.craftlife.eureka.server.internal;

import br.com.craftlife.eureka.loader.types.EurekaLoader;
import br.com.craftlife.eureka.resource.messages.Message;
import br.com.craftlife.eureka.server.ServerManager;
import br.com.craftlife.eureka.server.entity.Console;
import br.com.craftlife.eureka.server.entity.User;
import lombok.Getter;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class ServerManagerImpl extends ServerManager {

    protected final Map<String, User> users = new HashMap<>();

    private final EurekaLoader loader;

    private final NativeListener listener;

    @Getter
    private Console console;

    public ServerManagerImpl(EurekaLoader loader) {
        this.loader = loader;
        this.listener = new NativeListener(this);
    }

    public void init() {
        Server server = loader.getModule().getServer();
        this.setServer(server);
        console = new Console(server.getConsoleSender(), this);
        server.getPluginManager().registerEvents(listener, loader.getModule());
    }

    @Override
    public Optional<User> getUser(String name) {
        if (name == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(users.get(name.toLowerCase()));
    }

    @Override
    public Optional<User> getUser(Player player) {
        if (player == null) {
            return Optional.empty();
        }
        return getUser(player.getName());
    }

    @Override
    public Optional<User> getUser(UUID id) {
        if (id == null) {
            return Optional.empty();
        }
        return getUser(this.getServer().getPlayer(id));
    }

    @Override
    public Set<User> matchUser(String name) {
        String lname = name.toLowerCase();
        return users.values().stream()
                .filter(user -> user.getName().toLowerCase().contains(lname))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<User> getOnlineUsers() {
        return new HashSet<>(users.values());
    }

    @Override
    public void broadcastMessage(Message message) {
        getOnlineUsers().forEach(user -> user.sendMessage(message));
    }

    protected void addUser(Player player) {
        users.put(player.getName().toLowerCase(), new User(player, this));
    }

    protected void removeUser(String user) {
        users.remove(user.toLowerCase());
    }

}
