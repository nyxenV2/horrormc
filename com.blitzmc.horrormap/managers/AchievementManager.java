package com.blitzmc.horrormap.managers;

import org.bukkit.entity.Player;
import com.blitzmc.horrormap.BlitzHorrorMapPlugin;

public class AchievementManager {

    private BlitzHorrorMapPlugin plugin;

    public AchievementManager(BlitzHorrorMapPlugin plugin) {
        this.plugin = plugin;
    }

    public void awardAchievement(Player player, String achievement) {
        // Handle awarding achievements based on map-specific criteria
        player.sendMessage("§a[BlitzMC] Achievement unlocked: " + achievement);
        // Example: Store achievement in player data or database
    }

    public void checkAndAwardAchievements(Player player, String mapName, long completionTime, boolean allSurvived) {
        // Check for fast completion achievement
        if (completionTime < plugin.getConfig().getLong("maps." + mapName + ".achievement_time", Long.MAX_VALUE)) {
            awardAchievement(player, "Fast Completion on " + mapName);
        }

        // Check for team survival achievement
        if (allSurvived) {
            awardAchievement(player, "Full Team Survival on " + mapName);
        }
    }
}
