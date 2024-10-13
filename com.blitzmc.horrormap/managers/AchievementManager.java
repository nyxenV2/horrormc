package com.blitzmc.horrormap.managers;

import org.bukkit.entity.Player;
import com.blitzmc.horrormap.BlitzHorrorMapPlugin;

public class AchievementManager {

    private BlitzHorrorMapPlugin plugin;

    public AchievementManager(BlitzHorrorMapPlugin plugin) {
        this.plugin = plugin;
    }

    public void awardAchievement(Player player, String achievement) {
        player.sendMessage("§a[BlitzMC] Achievement unlocked: " + achievement);
    }

    public void checkAndAwardAchievements(Player player, String mapName, long completionTime, boolean allSurvived) {
        if (completionTime < plugin.getConfig().getLong("maps." + mapName + ".achievement_time", Long.MAX_VALUE)) {
            awardAchievement(player, "Fast Completion on " + mapName);
        }

        if (allSurvived) {
            awardAchievement(player, "Full Team Survival on " + mapName);
        }
    }
}
