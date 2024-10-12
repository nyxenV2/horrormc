package com.blitzmc.horrormap.managers;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import com.blitzmc.horrormap.BlitzHorrorMapPlugin;

public class InstanceManager {

    private BlitzHorrorMapPlugin plugin;

    public InstanceManager(BlitzHorrorMapPlugin plugin) {
        this.plugin = plugin;
    }

    public void createInstance(String mapName, String instanceName, Runnable onComplete) {
        File source = plugin.getMapManager().getMapFolder(mapName);
        if (source == null) {
            plugin.getLogger().log(Level.SEVERE, "Map not found: " + mapName);
            Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage("§c[BlitzMC] Error: Map " + mapName + " not found."));
            return;
        }

        File target = new File(Bukkit.getWorldContainer(), instanceName);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                FileUtils.copyDirectory(source, target);
                Bukkit.getScheduler().runTask(plugin, () -> {
                    World world = new WorldCreator(instanceName).createWorld();
                    plugin.getInstanceManager().addActiveWorld(instanceName, world);
                    onComplete.run();
                });
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to copy world: " + mapName, e);
                Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage("§c[BlitzMC] Failed to load the map. Try again later."));
            }
        });
    }

    public void deleteInstance(String instanceName, boolean recycle) {
        World world = plugin.getInstanceManager().getActiveWorld(instanceName);
        if (world != null) {
            Bukkit.unloadWorld(world, false);
        }

        File target = new File(Bukkit.getWorldContainer(), instanceName);
        if (!recycle) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                try {
                    FileUtils.deleteDirectory(target);
                } catch (IOException e) {
                    plugin.getLogger().log(Level.SEVERE, "Failed to delete world instance: " + instanceName, e);
                }
            });
        }
    }

    // Manage active worlds
    private Map<String, World> activeWorlds = new HashMap<>();

    public void addActiveWorld(String instanceName, World world) {
        activeWorlds.put(instanceName, world);
    }

    public World getActiveWorld(String instanceName) {
        return activeWorlds.get(instanceName);
    }
}
