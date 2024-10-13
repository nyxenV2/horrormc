package com.blitzmc.horrormap.managers;

import org.bukkit.entity.Player;
import com.blitzmc.horrormap.BlitzHorrorMapPlugin;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

public class QueueManager {

    private BlitzHorrorMapPlugin plugin;
    private Queue<UUID> playerQueue;

    public QueueManager(BlitzHorrorMapPlugin plugin) {
        this.plugin = plugin;
        this.playerQueue = new LinkedList<>();
    }

    public void addToQueue(Player player) {
        playerQueue.add(player.getUniqueId());
        player.sendMessage("§a[BlitzMC] You have been added to the session queue.");
    }

    public void processQueue() {
        if (!playerQueue.isEmpty()) {
            UUID playerId = playerQueue.poll();
            Player player = plugin.getServer().getPlayer(playerId);

            if (player != null && player.isOnline()) {
                plugin.getLobbyManager().teleportToLobby(player);
                player.sendMessage("§a[BlitzMC] You have been added to the next available session.");
            }
        }
    }
}
