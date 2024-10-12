package com.blitzmc.horrormap.events;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.EventHandler;
import com.blitzmc.horrormap.BlitzHorrorMapPlugin;

public class PlayerListener implements Listener {

    private BlitzHorrorMapPlugin plugin;

    public PlayerListener(BlitzHorrorMapPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getSessionManager().handlePlayerLeave(event.getPlayer());
    }
}
