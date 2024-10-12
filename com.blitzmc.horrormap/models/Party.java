package com.blitzmc.horrormap.models;

import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Party {

    private Player leader;
    private List<Player> members;
    private SessionSettings settings;
    private Set<Player> mapSelectionPermission;
    private Set<Player> startGamePermission;

    public Party(Player leader) {
        this.leader = leader;
        this.members = new ArrayList<>();
        this.members.add(leader);
        this.settings = new SessionSettings();
        this.mapSelectionPermission = new HashSet<>();
        this.startGamePermission = new HashSet<>();
    }

    public Player getLeader() {
        return leader;
    }

    public List<Player> getMembers() {
        return members;
    }

    public boolean hasMember(Player player) {
        return members.contains(player);
    }

    public void addMember(Player player) {
        members.add(player);
    }

    public void removeMember(Player player) {
        members.remove(player);
    }

    public void grantPermission(Player player, String permission) {
        switch (permission.toLowerCase()) {
            case "mapselection":
                mapSelectionPermission.add(player);
                break;
            case "startgame":
                startGamePermission.add(player);
                break;
            default:
                break;
        }
    }

    public void revokePermission(Player player, String permission) {
        switch (permission.toLowerCase()) {
            case "mapselection":
                mapSelectionPermission.remove(player);
                break;
            case "startgame":
                startGamePermission.remove(player);
                break;
            default:
                break;
        }
    }

    public boolean hasPermission(Player player, String permission) {
        switch (permission.toLowerCase()) {
            case "mapselection":
                return mapSelectionPermission.contains(player);
            case "startgame":
                return startGamePermission.contains(player);
            default:
                return false;
        }
    }

    public void broadcastMessage(String message) {
        for (Player member : members) {
            member.sendMessage(message);
        }
    }

    public void sendChatMessage(Player sender, String message) {
        for (Player member : members) {
            member.sendMessage("§e[Party] " + sender.getName() + ": " + message);
        }
    }

    public SessionSettings getSettings() {
        return settings;
    }
}
