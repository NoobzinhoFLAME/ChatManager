package com.noobzinhoflame.ChatManager.listener;

import com.noobzinhoflame.ChatManager.Main;
import com.noobzinhoflame.ChatManager.utils.cooldown.Cooldown;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;
import java.util.regex.Pattern;

public class ChatListener extends Cooldown implements Listener {

    public List<String> PalavrasOfensivas;
    private boolean PalavraOfensivaEncontrada = false;
    private final Pattern URL = Pattern.compile("(http://www\\.|https://www\\.|http://|https://)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(/.*)?", Pattern.CASE_INSENSITIVE);

    public ChatListener(List<String> palavrasOfensivas) {
        this.PalavrasOfensivas = palavrasOfensivas;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String message = e.getMessage();

        if (URL.matcher(message).find() && Main.getInstance().getConfig().getBoolean("Anti-Link") && !p.hasPermission("chatmanager.bypass") && !inCooldown(p)) {
            e.setCancelled(true);
            e.getPlayer().sendMessage("§cLinks não são permitidos no chat!");
        }

        if (p.hasPermission("chatmanager.chatcolor") && Main.getInstance().getConfig().getBoolean("Chat-Color")) {
            message = message.replaceAll("&", "§");
        }

        for (String word : PalavrasOfensivas) {
            if (message.toLowerCase().contains(word) && Main.getInstance().getConfig().getBoolean("AntiXingamentos") && !inCooldown(p) && !p.hasPermission("chatmanager.bypass")) {
                PalavraOfensivaEncontrada = true;
                message = message.replaceAll("(?i)" + word, censorWord(word));
            }
        }

        if (PalavraOfensivaEncontrada) {
            e.getPlayer().sendMessage("§4§lAVISO §cVocê utilizou uma palavra bloqueada no chat!");
        }

        e.setMessage(message);

        if (inCooldown(p)) {
            sendCooldown(p);
            e.setCancelled(true);
        } else if (!Main.getInstance().CHAT && !p.hasPermission("chatmanager.bypass")) {
            p.sendMessage("§cOps! O chat está desativado no momento.");
            e.setCancelled(true);
        } else if (!p.hasPermission("chatmanager.bypass") && Main.getInstance().getConfig().getBoolean("ChatCooldown")) {
            addCooldown(p, Main.getInstance().getConfig().getInt("ChatCooldownTime", 4));
        }
    }

    private String censorWord(String word){
        StringBuilder censored = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            censored.append("*");
        }
        return censored.toString();
    }
}
