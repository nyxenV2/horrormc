package com.blitzmc.horrormap.managers;

import org.bukkit.entity.Player;
import com.blitzmc.horrormap.BlitzHorrorMapPlugin;

public class PermissionManager {

    private BlitzHorrorMapPlugin plugin;

    public PermissionManager(BlitzHorrorMapPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean hasPermission(Player player, String permission) {
        return player.hasPermission(permission);
    }
}
