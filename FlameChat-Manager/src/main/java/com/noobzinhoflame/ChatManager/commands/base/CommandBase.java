package com.noobzinhoflame.ChatManager.commands.base;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public abstract class CommandBase extends Command {

    public CommandBase(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender sender, String lb, String[] args) {
        return false;
    }

    public void sendNoPermission(CommandSender sender) {
        sender.sendMessage("§cVocê não possui permissão para usar este comando.");
    }
}