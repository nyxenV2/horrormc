package com.blitzmc.horrormap.managers;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import com.blitzmc.horrormap.BlitzHorrorMapPlugin;

public class MapManager {

    private BlitzHorrorMapPlugin plugin;
    private Map<String, File> maps;

    public MapManager(BlitzHorrorMapPlugin plugin) {
        this.plugin = plugin;
        this.maps = new HashMap<>();
        loadMaps();
    }

    private void loadMaps() {
        File mapsDir = new File(plugin.getDataFolder(), "maps");
        if (!mapsDir.exists()) {
            mapsDir.mkdirs();
        }

        for (File mapFolder : mapsDir.listFiles()) {
            if (mapFolder.isDirectory()) {
                maps.put(mapFolder.getName(), mapFolder);
            }
        }
    }

    public File getMapFolder(String mapName) {
        return maps.get(mapName);
    }

    public Map<String, File> getMaps() {
        return maps;
    }
}
