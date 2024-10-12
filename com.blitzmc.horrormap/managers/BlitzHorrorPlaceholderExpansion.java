package com.blitzmc.horrormap.managers;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import com.blitzmc.horrormap.BlitzHorrorMapPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class BlitzHorrorPlaceholderExpansion extends PlaceholderExpansion {

    private BlitzHorrorMapPlugin plugin;

    public BlitzHorrorPlaceholderExpansion(BlitzHorrorMapPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "blitzhorrormap";
    }

    @Override
    public String getAuthor() {
        return "BlitzMC";
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        // Return player's session status
        if (identifier.equals("session_status")) {
            return getSessionStatus(player);
        }

        // Return player's leaderboard position for a specific map
        if (identifier.startsWith("leaderboard_position_")) {
            String mapName = identifier.split("leaderboard_position_")[1];
            return getLeaderboardPosition(player, mapName);
        }

        return null;
    }

    // Retrieve session status
    private String getSessionStatus(Player player) {
        return plugin.getSessionManager().getSessionByPlayer(player) != null ? "In Session" : "Not in Session";
    }

    // Retrieve leaderboard position for a specific map
    private String getLeaderboardPosition(Player player, String mapName) {
        Connection databaseConnection = plugin.getLeaderboardManager().getDatabaseConnection();
        if (databaseConnection == null) {
            return "N/A";
        }

        UUID playerUUID = player.getUniqueId();
        String position = "N/A";

        try (PreparedStatement statement = databaseConnection.prepareStatement(
                "SELECT player_uuid, ROW_NUMBER() OVER (ORDER BY wins DESC) AS position " +
                        "FROM leaderboards WHERE map_name = ?"
        )) {
            statement.setString(1, mapName);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                UUID currentPlayerUUID = UUID.fromString(resultSet.getString("player_uuid"));
                int currentPosition = resultSet.getInt("position");

                if (currentPlayerUUID.equals(playerUUID)) {
                    position = String.valueOf(currentPosition);
                    break;
                }
            }

        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to retrieve leaderboard position for " + player.getName() + " on map " + mapName);
        }

        return position;
    }
}
