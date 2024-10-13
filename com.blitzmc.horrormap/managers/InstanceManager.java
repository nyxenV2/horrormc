package com.blitzmc.horrormap.managers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import com.blitzmc.horrormap.BlitzHorrorMapPlugin;

public class InstanceManager {

    private BlitzHorrorMapPlugin plugin;
    private Map<String, World> activeWorlds;

    public InstanceManager(BlitzHorrorMapPlugin plugin) {
        this.plugin = plugin;
        this.activeWorlds = new HashMap<>();
    }

    public void createInstance(String mapName, String instanceName, Runnable onComplete) {
        File source = plugin.getMapManager().getMapFolder(mapName);
        if (source == null) {
            plugin.getLogger().log(Level.SEVERE, "Map not found: " + mapName);
            Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage("§c[BlitzMC] Error: Map " + mapName + " not found."));
            return;
        }

        File target = new File(Bukkit.getWorldContainer(), instanceName);
        if (activeWorlds.containsKey(instanceName)) {
            plugin.getLogger().log(Level.INFO, "World instance already exists: " + instanceName);
            onComplete.run();
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                FileUtils.copyDirectory(source, target);
                Bukkit.getScheduler().runTask(plugin, () -> {
                    World world = new WorldCreator(instanceName).createWorld();
                    activeWorlds.put(instanceName, world);
                    onComplete.run();
                });
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to copy world: " + mapName, e);
                Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage("§c[BlitzMC] Failed to load the map. Try again later."));
            }
        });
    }

    public void deleteInstance(String instanceName, boolean recycle) {
        World world = activeWorlds.remove(instanceName);
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

    public World getActiveWorld(String instanceName) {
        return activeWorlds.get(instanceName);
    }
}
