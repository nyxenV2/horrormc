package com.blitzmc.horrormap.managers;

import java.util.HashMap;
import java.util.UUID;
import org.bukkit.entity.Player;
import com.blitzmc.horrormap.BlitzHorrorMapPlugin;
import com.blitzmc.horrormap.models.Party;

public class PartyManager {

    private BlitzHorrorMapPlugin plugin;
    private HashMap<UUID, Party> parties;
    private HashMap<UUID, UUID> pendingInvites; // Maps player IDs to the party leader who invited them

    public PartyManager(BlitzHorrorMapPlugin plugin) {
        this.plugin = plugin;
        this.parties = new HashMap<>();
        this.pendingInvites = new HashMap<>();
    }

    public Party createParty(Player leader) {
        Party party = new Party(leader);
        parties.put(leader.getUniqueId(), party);
        leader.sendMessage("§a[BlitzMC] Party created! You are the leader.");
        return party;
    }

    public Party getParty(Player player) {
        for (Party party : parties.values()) {
            if (party.hasMember(player)) {
                return party;
            }
        }
        return null;
    }

    public void removeParty(Party party) {
        parties.remove(party.getLeader().getUniqueId());
        party.broadcastMessage("§c[BlitzMC] Party disbanded.");
    }

    public void sendInvite(Player leader, Player invitee) {
        if (!parties.containsKey(leader.getUniqueId())) {
            leader.sendMessage("§c[BlitzMC] You must be a party leader to invite players.");
            return;
        }
        if (pendingInvites.containsKey(invitee.getUniqueId())) {
            leader.sendMessage("§c[BlitzMC] That player already has a pending invite.");
            return;
        }

        invitee.sendMessage(leader.getName() + " has invited you to their party. Use /party accept or /party decline.");
        pendingInvites.put(invitee.getUniqueId(), leader.getUniqueId());
    }

    public void acceptInvite(Player invitee) {
        UUID leaderId = pendingInvites.remove(invitee.getUniqueId());
        if (leaderId == null) {
            invitee.sendMessage("§c[BlitzMC] You do not have any pending invites.");
            return;
        }

        Party party = parties.get(leaderId);
        if (party != null && !party.hasMember(invitee)) {
            if (party.getMembers().size() >= party.getSettings().getMaxPartySize()) {
                invitee.sendMessage("§c[BlitzMC] This party is full.");
                return;
            }
            party.addMember(invitee);
            party.broadcastMessage(invitee.getName() + " has joined the party.");
        } else {
            invitee.sendMessage("§c[BlitzMC] The party no longer exists.");
        }
    }

    public void declineInvite(Player invitee) {
        if (pendingInvites.remove(invitee.getUniqueId()) != null) {
            invitee.sendMessage("§c[BlitzMC] You have declined the party invite.");
        } else {
            invitee.sendMessage("§c[BlitzMC] You do not have any pending invites.");
        }
    }

    public void sendPartyChatMessage(Player sender, String message) {
        Party party = getParty(sender);
        if (party != null) {
            party.sendChatMessage(sender, message);
        } else {
            sender.sendMessage("§c[BlitzMC] You are not in a party.");
        }
    }
}
