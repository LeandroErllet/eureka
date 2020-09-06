package br.com.craftlife.eureka.server.entity.internal;

import br.com.craftlife.eureka.server.ServerManager;
import lombok.experimental.Delegate;
import org.bukkit.Server;
import org.bukkit.attribute.Attributable;
import org.bukkit.conversations.Conversable;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public abstract class UserDelegateImpl extends SenderImpl {

    private interface Ignored {
        void sendRawMessage(String x);

        void sendMessage(String x);

        void sendMessage(String[] x);

        boolean hasPermission(String x);

        boolean hasPermission(Permission x);

        Server getServer();
    }

    @Delegate(excludes = {Ignored.class, Conversable.class, Attributable.class})
    private final Player player;

    protected UserDelegateImpl(Player player, ServerManager serverManager) {
        super(player, serverManager);
        this.player = player;
    }

}
