package com.noobzinhoflame.ChatManager.commands;

import com.noobzinhoflame.ChatManager.Main;
import com.noobzinhoflame.ChatManager.commands.base.CommandBase;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatCommand extends CommandBase {

    public ChatCommand() {
        super("chat");
    }

    @Override
    public boolean execute(CommandSender sender, String st, String[] args) {
        if (!sender.hasPermission("chatmanager.commands.chat")) {
            sendNoPermission(sender);
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("§cUso: /chat [on, off, clear]");
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "on":
                On(sender);
                break;
            case "off":
                Off(sender);
                break;
            case "clear":
                Clear();
                break;
            default:
                sender.sendMessage("§cUso: /chat [on, off, clear]");
                return false;
        }
        return false;
    }
        private void Clear() {
        for (int i = 0; i < 100; i++)
            for (Player o : Bukkit.getOnlinePlayers()) {
                o.sendMessage("§b§l§e");
            }
        }

    private boolean On(CommandSender sender) {
        if (Main.getInstance().CHAT) {
            sender.sendMessage("§cO chat já está ativado.");
            return false;
        }
        Main.getInstance().CHAT = true;
        Bukkit.broadcastMessage("§aO chat foi ativado.");
        return true;
    }

    private boolean Off(CommandSender sender) {
        if (!Main.getInstance().CHAT) {
            sender.sendMessage("§cO chat já está desativado.");
            return false;
        }
        Main.getInstance().CHAT = false;
        Bukkit.broadcastMessage("§cO chat foi desativado.");
        return true;
    }
}