package com.blitzmc.horrormap.managers;

import de.maxhenkel.voicechat.api.ServerVoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import com.blitzmc.horrormap.BlitzHorrorMapPlugin;
import com.blitzmc.horrormap.models.Session;

import java.util.logging.Level;

public class VoiceChatManager {

    private BlitzHorrorMapPlugin plugin;
    private VoicechatApi voicechatApi;

    public VoiceChatManager(BlitzHorrorMapPlugin plugin) {
        this.plugin = plugin;
        this.voicechatApi = ServerVoicechatApi.instance();
    }

    public void enableVoiceChat(Session session) {
        VoicechatServer voicechatServer = voicechatApi.getVoicechatServer();
        try {
            voicechatServer.createGroup(session.getInstanceName(), session.getParty().getMembers());
            plugin.getLogger().info("§a[BlitzMC] Voice chat enabled for session " + session.getInstanceName());
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to enable voice chat for session " + session.getInstanceName(), e);
            session.getParty().broadcastMessage("§c[BlitzMC] Error: Failed to enable voice chat.");
        }
    }

    public void disableVoiceChat(Session session) {
        VoicechatServer voicechatServer = voicechatApi.getVoicechatServer();
        try {
            voicechatServer.deleteGroup(session.getInstanceName());
            plugin.getLogger().info("§a[BlitzMC] Voice chat disabled for session " + session.getInstanceName());
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to disable voice chat for session " + session.getInstanceName(), e);
        }
    }
}
