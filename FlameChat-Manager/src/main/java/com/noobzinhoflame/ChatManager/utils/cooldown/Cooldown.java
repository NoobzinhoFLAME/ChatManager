package com.noobzinhoflame.ChatManager.utils.cooldown;

import com.noobzinhoflame.ChatManager.Main;
import com.noobzinhoflame.ChatManager.events.custom.TimeSecondEvent;
import com.noobzinhoflame.ChatManager.utils.nms.ActionBarAPI;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Cooldown {

    private final ConcurrentHashMap<UUID, Long> cooldown = new ConcurrentHashMap<>();
    private final DecimalFormat FORMATTER = new DecimalFormat("#.#");
    private double cooldownSeconds;

    private void display(Player player, double seconds, double cooldownSeconds) {
        StringBuilder barras = new StringBuilder();
        double porcentagem = Math.max(0, Math.min(seconds / cooldownSeconds, 1.0)) * 30;
        int filled = (int) porcentagem;

        for (int i = 0; i < 30; i++) {
            if (i < (30 - filled)) {
                barras.append("§a|");
            } else {
                barras.append("§c|");
            }
        }

        ActionBarAPI action = new ActionBarAPI();

        if (inCooldown(player))
            action.send(player, "Chat-Cooldown " + barras + " §f" + String.format("%.1f segundos", seconds));
    }

    private void clearActionBar(Player player) {
        ActionBarAPI action = new ActionBarAPI();
        action.send(player, "");
    }

    public boolean inCooldown(Player player) {
        return cooldown.containsKey(player.getUniqueId());
    }

    public void addCooldown(Player player, double seconds) {
        cooldown.put(player.getUniqueId(), System.currentTimeMillis());

        cooldownSeconds = seconds;
    }

    public void sendCooldown(Player player) {
        double seconds = ((cooldown.get(player.getUniqueId()) / 1000.0) + cooldownSeconds) - (System.currentTimeMillis() / 1000.0);
        player.sendMessage("§cAguarde " + FORMATTER.format(seconds) + "s para conversar novamente!");
    }

    @EventHandler
    public void onTimeMillisecond(TimeSecondEvent event) {
        if (event.getType() == TimeSecondEvent.TimeType.MILLISECONDS) {
            Iterator<Map.Entry<UUID, Long>> iterator = cooldown.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<UUID, Long> entry = iterator.next();
                UUID uuid = entry.getKey();
                long startTime = entry.getValue();

                double seconds = ((startTime / 1000.0D) + cooldownSeconds) - (System.currentTimeMillis() / 1000.0D);
                Player player = Bukkit.getPlayer(uuid);
                if (player != null && Main.getInstance().getConfig().getBoolean("ActionDisplayCooldown")) {
                    if (seconds <= 0.0D) {
                        iterator.remove();
                        clearActionBar(player);
                        String soundName = Main.getInstance().getConfig().getString("Sound");
                        if (!soundName.isEmpty()) {
                            Sound sound = Sound.valueOf(soundName);
                            player.playSound(player.getLocation(), sound, 1f, 1f);
                        }
                    }
                    if (player != null) {
                        display(player, seconds, cooldownSeconds);
                    }
                }
            }
        }
    }
}


