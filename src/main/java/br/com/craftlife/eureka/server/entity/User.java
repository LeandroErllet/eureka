package br.com.craftlife.eureka.server.entity;

import br.com.craftlife.eureka.server.ServerManager;
import br.com.craftlife.eureka.server.entity.internal.UserDelegateImpl;
import lombok.NonNull;
import org.bukkit.entity.Player;

public class User extends UserDelegateImpl {

    private final Player player;

    private final ServerManager serverManager;

    public User(@NonNull Player player, @NonNull ServerManager serverManager) {
        super(player, serverManager);
        this.player = player;
        this.serverManager = serverManager;
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    public String getIpAddress() {
        return player.getAddress().getAddress().getHostAddress().trim();
    }

}
