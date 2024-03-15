package com.noobzinhoflame.ChatManager;

import com.noobzinhoflame.ChatManager.commands.ChatCommand;
import com.noobzinhoflame.ChatManager.events.custom.TimeSecondEvent;
import com.noobzinhoflame.ChatManager.listener.ChatListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public final class Main extends JavaPlugin {

    private FileConfiguration config;
    private List<String> PalavrasOfensivas;

    @Getter
    public static Main instance;
    @Getter
    public boolean CHAT = true;

    @Override
    public void onEnable() {
        instance = this;
        loadConfig();

        Bukkit.getPluginManager().registerEvents(new ChatListener(PalavrasOfensivas), this);

        CommandMap map = ((CraftServer) getServer()).getCommandMap();
        map.register("chat", new ChatCommand());

        if (getConfig().getBoolean("MensagensAutomaticas")) {
            MensagensAutomaticas();
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                new TimeSecondEvent(TimeSecondEvent.TimeType.MILLISECONDS).call();
            }
        }.runTaskTimer(this, 0L, 1L);
    }

    @Override
    public void onDisable() {}

    private void MensagensAutomaticas() {
        List<String> messages = config.getStringList("messages");

        Bukkit.getScheduler().runTaskTimer(this, new BukkitRunnable() {
            private int currentMessageIndex = 0;

            @EventHandler
            public void run() {
                if (!messages.isEmpty()) {
                    String message = messages.get(currentMessageIndex);

                    if (message != null && !message.isEmpty()) {
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
                    }

                    currentMessageIndex++;

                    if (currentMessageIndex >= messages.size()) {
                        currentMessageIndex = 0;
                    }
                }
            }
        }, 0, config.getInt("MensagensCooldown", 300) * 20);
    }

    private void loadConfig() {
        saveDefaultConfig();
        PalavrasOfensivas = getConfig().getStringList("PalavrasOfensivas");

        File configFile = new File(getDataFolder(), "config.yml");
        try {
            config = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(configFile), StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
