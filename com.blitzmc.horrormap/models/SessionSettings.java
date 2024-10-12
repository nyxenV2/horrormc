package com.blitzmc.horrormap.models;

public class SessionSettings {

    private int playerCount;
    private boolean voiceChatEnabled;
    private boolean proximityVoiceChat;
    private boolean spectatorsAllowed;
    private String weather;
    private String timeOfDay;
    private String difficulty;
    private String resourcePackURL;
    private int maxPartySize;

    public SessionSettings() {
        // Set default settings
        this.playerCount = 4;
        this.voiceChatEnabled = false;
        this.proximityVoiceChat = true;
        this.spectatorsAllowed = false;
        this.weather = "clear";
        this.timeOfDay = "night";
        this.difficulty = "normal";
        this.resourcePackURL = null;
        this.maxPartySize = 4; // Default maximum party size
    }

    // Getters and setters for each setting

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public boolean isVoiceChatEnabled() {
        return voiceChatEnabled;
    }

    public void setVoiceChatEnabled(boolean voiceChatEnabled) {
        this.voiceChatEnabled = voiceChatEnabled;
    }

    public boolean isProximityVoiceChat() {
        return proximityVoiceChat;
    }

    public void setProximityVoiceChat(boolean proximityVoiceChat) {
        this.proximityVoiceChat = proximityVoiceChat;
    }

    public boolean isSpectatorsAllowed() {
        return spectatorsAllowed;
    }

    public void setSpectatorsAllowed(boolean spectatorsAllowed) {
        this.spectatorsAllowed = spectatorsAllowed;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(String timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getResourcePackURL() {
        return resourcePackURL;
    }

    public void setResourcePackURL(String resourcePackURL) {
        this.resourcePackURL = resourcePackURL;
    }

    public int getMaxPartySize() {
        return maxPartySize;
    }

    public void setMaxPartySize(int maxPartySize) {
        this.maxPartySize = maxPartySize;
    }
}
