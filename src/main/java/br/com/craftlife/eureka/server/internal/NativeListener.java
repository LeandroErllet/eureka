package br.com.craftlife.eureka.server.internal;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

class NativeListener implements Listener {

    private final ServerManagerImpl serverManager;

    NativeListener(ServerManagerImpl serverManager) {
        this.serverManager = serverManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        serverManager.addUser(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        serverManager.removeUser(event.getPlayer().getName());
    }

}
