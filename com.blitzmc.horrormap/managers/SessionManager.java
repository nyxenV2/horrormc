package com.blitzmc.horrormap.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import com.blitzmc.horrormap.BlitzHorrorMapPlugin;
import com.blitzmc.horrormap.models.Party;
import com.blitzmc.horrormap.models.Session;

public class SessionManager {

    private BlitzHorrorMapPlugin plugin;
    private Map<UUID, Session> activeSessions;
    private Map<String, Integer> worldLoadMap; // Keeps track of load per world instance

    public SessionManager(BlitzHorrorMapPlugin plugin) {
        this.plugin = plugin;
        this.activeSessions = new HashMap<>();
        this.worldLoadMap = new HashMap<>();
    }

    // Start a session with dynamic resource pack loading
    public void startSession(Party party, String mapName) {
        String instanceName = getLeastLoadedWorld(mapName); // Load-balancing logic
        Session session = new Session(party, mapName, instanceName);
        activeSessions.put(party.getLeader().getUniqueId(), session);

        plugin.getInstanceManager().createInstance(mapName, instanceName, () -> {
            World world = plugin.getInstanceManager().activeWorlds.get(instanceName);

            Bukkit.getScheduler().runTask(plugin, () -> {
                for (Player player : party.getMembers()) {
                    player.teleport(world.getSpawnLocation());
                    String resourcePackURL = party.getSettings().getResourcePackURL();

                    if (resourcePackURL != null) {
                        player.setResourcePack(resourcePackURL);
                    } else {
                        player.setResourcePack(plugin.getConfig().getString("default_resourcepack"));
                    }
                }

                if (party.getSettings().isVoiceChatEnabled()) {
                    plugin.getVoiceChatManager().enableVoiceChat(session);
                }
                plugin.getLogger().info("§a[BlitzMC] Session started for party led by " + party.getLeader().getName() + " on map " + mapName);
            });
        });
    }

    public void cleanupAllSessions() {
        for (Session session : activeSessions.values()) {
            cleanupSession(session, false);
        }
        activeSessions.clear();
        plugin.getLogger().info("§a[BlitzMC] All sessions have been cleaned up.");
    }

    public void cleanupSession(Session session, boolean recycle) {
        plugin.getVoiceChatManager().disableVoiceChat(session);
        Bukkit.getScheduler().runTask(plugin, () -> {
            for (Player player : session.getParty().getMembers()) {
                plugin.getLobbyManager().teleportToLobby(player);
            }
            plugin.getInstanceManager().deleteInstance(session.getInstanceName(), recycle);
        });
        activeSessions.remove(session.getParty().getLeader().getUniqueId());
        plugin.getLogger().info("§a[BlitzMC] Session " + session.getInstanceName() + " has been cleaned up.");
    }

    private String getLeastLoadedWorld(String mapName) {
        // Load balancing logic here
        return "someInstance";
    }

    public Session getSessionByPlayer(Player player) {
        for (Session session : activeSessions.values()) {
            if (session.getParty().hasMember(player)) {
                return session;
            }
        }
        return null;
    }
}
