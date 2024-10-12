package com.blitzmc.horrormap.managers;

import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.blitzmc.horrormap.BlitzHorrorMapPlugin;

public class LobbyManager {

    private BlitzHorrorMapPlugin plugin;
    private Map<UUID, Inventory> lobbyInventories;
    private Map<String, String> resourcePacks; // Map name -> Resource pack URL

    public LobbyManager(BlitzHorrorMapPlugin plugin) {
        this.plugin = plugin;
        this.lobbyInventories = new java.util.HashMap<>();
        this.resourcePacks = plugin.getConfig().getConfigurationSection("resourcepacks").getValues(false);
    }

    // Opens the lobby GUI for the player with the map and resource pack selection menu
    public void openLobbyGUI(Player player) {
        Map<String, ItemStack> maps = getMapsAsItemStacks();
        int guiSize = Math.max(9, ((maps.size() / 9) + 1) * 9);
        Inventory gui = Bukkit.createInventory(null, guiSize, "BlitzMC Horror Map Lobby");

        int slot = 0;
        for (ItemStack mapItem : maps.values()) {
            if (slot < guiSize) {
                gui.setItem(slot++, mapItem);
            }
        }

        player.openInventory(gui);
        lobbyInventories.put(player.getUniqueId(), gui);
    }

    // Fetch maps as ItemStacks to display in the GUI
    private Map<String, ItemStack> getMapsAsItemStacks() {
        Map<String, ItemStack> mapItems = new java.util.HashMap<>();
        Map<String, java.io.File> maps = plugin.getMapManager().getMaps();

        for (String mapName : maps.keySet()) {
            ItemStack mapItem = new ItemStack(Material.MAP);
            ItemMeta meta = mapItem.getItemMeta();
            meta.setDisplayName(mapName);
            meta.setLore(java.util.Arrays.asList("Click to select", "Map: " + mapName));
            mapItem.setItemMeta(meta);
            mapItems.put(mapName, mapItem);
        }

        return mapItems;
    }

    public void setResourcePackForSession(Player player, String mapName) {
        Party party = plugin.getPartyManager().getParty(player);
        if (party != null) {
            String resourcePackURL = resourcePacks.get(mapName);
            if (resourcePackURL != null) {
                party.getSettings().setResourcePackURL(resourcePackURL);
                player.sendMessage("§a[BlitzMC] Resource pack for " + mapName + " has been set.");
            } else {
                player.sendMessage("§c[BlitzMC] No resource pack available for this map. Using default pack.");
                party.getSettings().setResourcePackURL(plugin.getConfig().getString("default_resourcepack"));
            }
        } else {
            player.sendMessage("§c[BlitzMC] You are not in a party.");
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase("BlitzMC Horror Map Lobby")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem != null && clickedItem.getType() == Material.MAP) {
                String selectedMap = clickedItem.getItemMeta().getDisplayName();
                setResourcePackForSession(player, selectedMap);

                Party party = plugin.getPartyManager().getParty(player);
                if (party != null) {
                    party.getSettings().setMapName(selectedMap);
                    player.sendMessage("§a[BlitzMC] The map " + selectedMap + " has been set for your session.");
                } else {
                    player.sendMessage("§c[BlitzMC] You are not in a party.");
                }

                player.closeInventory();
            }
        }
    }

    public void removeLobbyInventory(Player player) {
        lobbyInventories.remove(player.getUniqueId());
    }
}
