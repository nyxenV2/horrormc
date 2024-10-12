package com.blitzmc.horrormap;

import org.bukkit.plugin.java.JavaPlugin;
import com.blitzmc.horrormap.commands.BlitzHorrorCommand;
import com.blitzmc.horrormap.events.PlayerListener;
import com.blitzmc.horrormap.managers.*;

public class BlitzHorrorMapPlugin extends JavaPlugin {

    private LobbyManager lobbyManager;
    private MapManager mapManager;
    private PartyManager partyManager;
    private PermissionManager permissionManager;
    private InstanceManager instanceManager;
    private SessionManager sessionManager;
    private VoiceChatManager voiceChatManager;
    private SpectatorManager spectatorManager;
    private LeaderboardManager leaderboardManager;
    private QueueManager queueManager;
    private EconomyManager economyManager;
    private AchievementManager achievementManager;

    @Override
    public void onEnable() {
        // Initialize managers
        this.lobbyManager = new LobbyManager(this);
        this.mapManager = new MapManager(this);
        this.partyManager = new PartyManager(this);
        this.permissionManager = new PermissionManager(this);
        this.instanceManager = new InstanceManager(this);
        this.sessionManager = new SessionManager(this);
        this.voiceChatManager = new VoiceChatManager(this);
        this.spectatorManager = new SpectatorManager(this);
        this.leaderboardManager = new LeaderboardManager(this);
        this.queueManager = new QueueManager(this);
        this.economyManager = new EconomyManager(this);
        this.achievementManager = new AchievementManager(this);

        // Register commands and events
        getCommand("blitzhorror").setExecutor(new BlitzHorrorCommand(this));
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        // PlaceholderAPI integration
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new BlitzHorrorPlaceholderExpansion(this).register();
        }

        // Console message for author and plugin info
        getLogger().info("=======================================");
        getLogger().info("Advanced Horror Plugin by BlitzMC Team");
        getLogger().info("Author: Moses Alfred");
        getLogger().info("Plugin Version: " + getDescription().getVersion());
        getLogger().info("Thank you for using the Advanced Horror Plugin!");
        getLogger().info("=======================================");

        // Load configurations
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Cleanup sessions and instances
        sessionManager.cleanupAllSessions();
    }

    // Getters for managers
    public LobbyManager getLobbyManager() { return lobbyManager; }
    public MapManager getMapManager() { return mapManager; }
    public PartyManager getPartyManager() { return partyManager; }
    public PermissionManager getPermissionManager() { return permissionManager; }
    public InstanceManager getInstanceManager() { return instanceManager; }
    public SessionManager getSessionManager() { return sessionManager; }
    public VoiceChatManager getVoiceChatManager() { return voiceChatManager; }
    public SpectatorManager getSpectatorManager() { return spectatorManager; }
    public LeaderboardManager getLeaderboardManager() { return leaderboardManager; }
    public QueueManager getQueueManager() { return queueManager; }
    public EconomyManager getEconomyManager() { return economyManager; }
    public AchievementManager getAchievementManager() { return achievementManager; }
}
