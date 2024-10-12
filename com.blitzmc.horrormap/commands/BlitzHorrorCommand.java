package com.blitzmc.horrormap.commands;

import org.bukkit.entity.Player;
import com.blitzmc.horrormap.BlitzHorrorMapPlugin;
import com.blitzmc.horrormap.models.Party;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BlitzHorrorCommand implements CommandExecutor {

    private BlitzHorrorMapPlugin plugin;

    public BlitzHorrorCommand(BlitzHorrorMapPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use BlitzMC commands.");
            return true;
        }

        if (args.length == 0) {
            plugin.getLobbyManager().openLobbyGUI(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "create":
                Party party = plugin.getPartyManager().createParty(player);
                player.sendMessage("§a[BlitzMC] Party created! You are the leader.");
                break;
            case "invite":
                if (args.length < 2) {
                    player.sendMessage("§c[BlitzMC] Usage: /blitzhorror invite <player>");
                    return true;
                }
                Player target = plugin.getServer().getPlayer(args[1]);
                if (target != null) {
                    plugin.getPartyManager().sendInvite(player, target);
                } else {
                    player.sendMessage("§c[BlitzMC] Player not found.");
                }
                break;
            case "start":
                if (args.length < 2) {
                    player.sendMessage("§c[BlitzMC] Usage: /blitzhorror start <mapname>");
                    return true;
                }
                plugin.getSessionManager().startSession(plugin.getPartyManager().getParty(player), args[1]);
                break;
            case "stats":
                plugin.getLeaderboardManager().showPlayerStats(player);
                break;
            case "leaderboard":
                if (args.length < 2) {
                    player.sendMessage("§c[BlitzMC] Usage: /blitzhorror leaderboard <mapname>");
                    return true;
                }
                plugin.getLeaderboardManager().showLeaderboard(player, args[1]);
                break;
            default:
                player.sendMessage("§c[BlitzMC] Unknown command.");
        }
        return true;
    }
}
