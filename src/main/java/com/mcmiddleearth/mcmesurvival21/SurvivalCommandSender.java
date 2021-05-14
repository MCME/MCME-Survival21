package com.mcmiddleearth.mcmesurvival21;

import com.mcmiddleearth.command.McmeCommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class SurvivalCommandSender implements McmeCommandSender {

    private CommandSender sender;

    public SurvivalCommandSender(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public void sendMessage(BaseComponent[] baseComponents) {
        sender.sendMessage(baseComponents);
    }

    public boolean hasPermission(String node) {
        return sender instanceof ConsoleCommandSender
                || sender.hasPermission(node);
    }

    public boolean isPlayer() {
        return sender instanceof Player;
    }

    public Player getPlayer() {
        return (Player) sender;
    }
}
