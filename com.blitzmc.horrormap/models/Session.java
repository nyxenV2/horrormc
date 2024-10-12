package com.blitzmc.horrormap.models;

import org.bukkit.World;

public class Session {

    private Party party;
    private String mapName;
    private String instanceName;
    private World world;

    public Session(Party party, String mapName, String instanceName, World world) {
        this.party = party;
        this.mapName = mapName;
        this.instanceName = instanceName;
        this.world = world;
    }

    public Party getParty() {
        return party;
    }

    public String getMapName() {
        return mapName;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public World getWorld() {
        return world;
    }
}
