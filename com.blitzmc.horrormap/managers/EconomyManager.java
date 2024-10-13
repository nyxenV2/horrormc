package com.blitzmc.horrormap.managers;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import com.blitzmc.horrormap.BlitzHorrorMapPlugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyManager {

    private BlitzHorrorMapPlugin plugin;
    private Economy economy;

    public EconomyManager(BlitzHorrorMapPlugin plugin) {
        this.plugin = plugin;
        setupEconomy();
    }

    private boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        economy = rsp.getProvider();
        return economy != null;
    }

    public boolean chargePlayer(Player player, double amount) {
        if (economy == null) {
            return false;
        }
        if (economy.has(player, amount)) {
            economy.withdrawPlayer(player, amount);
            player.sendMessage("§a[BlitzMC] You have been charged $" + amount + " to join the session.");
            return true;
        } else {
            player.sendMessage("§c[BlitzMC] You do not have enough money to join the session.");
            return false;
        }
    }
}
