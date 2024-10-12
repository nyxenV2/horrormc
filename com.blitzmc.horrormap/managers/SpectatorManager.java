package com.blitzmc.horrormap.managers;

import java.util.HashSet;
import java.util.Set;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import com.blitzmc.horrormap.BlitzHorrorMapPlugin;
import com.blitzmc.horrormap.models.Session;

public class SpectatorManager {

    private BlitzHorrorMapPlugin plugin;
    private Set<Player> spectators;

    public SpectatorManager(BlitzHorrorMapPlugin plugin) {
        this.plugin = plugin;
        this.spectators = new HashSet<>();
    }

    public void addSpectator(Player player, Session session) {
        player.teleport(session.getWorld().getSpawnLocation());
        player.setGameMode(GameMode.SPECTATOR);
        spectators.add(player);
    }

    public void removeSpectator(Player player) {
        player.setGameMode(GameMode.SURVIVAL);
        plugin.getLobbyManager().teleportToLobby(player);
        spectators.remove(player);
    }
}
