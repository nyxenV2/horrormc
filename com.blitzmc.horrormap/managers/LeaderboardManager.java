package com.blitzmc.horrormap.managers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.blitzmc.horrormap.BlitzHorrorMapPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class LeaderboardManager {

    private BlitzHorrorMapPlugin plugin;
    private Connection databaseConnection;

    public LeaderboardManager(BlitzHorrorMapPlugin plugin) {
        this.plugin = plugin;
        this.databaseConnection = setupDatabase();
        createTablesIfNotExists();
    }

    private Connection setupDatabase() {
        try {
            return Bukkit.getPluginManager().getPlugin("SomeDatabasePlugin").getDatabase().getConnection();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to connect to database.");
        }
        return null;
    }

    private void createTablesIfNotExists() {
        try (PreparedStatement statement = databaseConnection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS leaderboards (" +
                "player_uuid VARCHAR(36), " +
                "map_name VARCHAR(255), " +
                "wins INT DEFAULT 0, " +
                "losses INT DEFAULT 0, " +
                "completion_time INT DEFAULT 0, " +
                "PRIMARY KEY (player_uuid, map_name))"
        )) {
            statement.execute();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to create leaderboard table.");
        }
    }

    public void recordSession(UUID playerId, String mapName, boolean won, int completionTime) {
        try (PreparedStatement statement = databaseConnection.prepareStatement(
                "INSERT INTO leaderboards (player_uuid, map_name, wins, losses, completion_time) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE wins = wins + ?, losses = losses + ?, completion_time = LEAST(completion_time, ?)"
        )) {
            statement.setString(1, playerId.toString());
            statement.setString(2, mapName);
            statement.setInt(3, won ? 1 : 0);
            statement.setInt(4, won ? 0 : 1);
            statement.setInt(5, completionTime);
            statement.setInt(6, won ? 1 : 0);
            statement.setInt(7, won ? 0 : 1);
            statement.setInt(8, completionTime);
            statement.execute();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to record leaderboard data.");
        }
    }

    public void showLeaderboard(Player player, String mapName) {
        Inventory leaderboardGui = Bukkit.createInventory(null, 27, "Leaderboard for " + mapName);

        try (PreparedStatement statement = databaseConnection.prepareStatement(
                "SELECT player_uuid, wins, losses, completion_time FROM leaderboards WHERE map_name = ? ORDER BY wins DESC LIMIT 10"
        )) {
            statement.setString(1, mapName);
            ResultSet resultSet = statement.executeQuery();
            int slot = 0;
            while (resultSet.next() && slot < 27) {
                String playerName = Bukkit.getOfflinePlayer(UUID.fromString(resultSet.getString("player_uuid"))).getName();
                int wins = resultSet.getInt("wins");
                int losses = resultSet.getInt("losses");
                int completionTime = resultSet.getInt("completion_time");

                ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
                ItemMeta meta = playerHead.getItemMeta();
                meta.setDisplayName(playerName);
                meta.setLore(java.util.Arrays.asList("Wins: " + wins, "Losses: " + losses, "Best Time: " + completionTime));
                playerHead.setItemMeta(meta);

                leaderboardGui.setItem(slot++, playerHead);
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to load leaderboard data.");
        }

        player.openInventory(leaderboardGui);
    }

    public void showPlayerStats(Player player) {
        try (PreparedStatement statement = databaseConnection.prepareStatement(
                "SELECT map_name, wins, losses, completion_time FROM leaderboards WHERE player_uuid = ?"
        )) {
            statement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = statement.executeQuery();

            player.sendMessage("§a[BlitzMC] Your Stats:");
            while (resultSet.next()) {
                String mapName = resultSet.getString("map_name");
                int wins = resultSet.getInt("wins");
                int losses = resultSet.getInt("losses");
                int completionTime = resultSet.getInt("completion_time");

                player.sendMessage("§eMap: " + mapName + " | Wins: " + wins + " | Losses: " + losses + " | Best Time: " + completionTime + "s");
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to fetch player statistics.");
        }
    }

    public Connection getDatabaseConnection() {
        return databaseConnection;
    }
}
