package com.mcmiddleearth.mcmesurvival21.outpost;

import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.mcmesurvival21.SurvivalCommandSender;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OutpostManager {

    private final static List<Outpost> outposts = new ArrayList<>();

    public static List<Outpost> getOutposts() {
        return outposts;
    }

    public static void init() {
        for(File file: Outpost.getOutpostFolder().listFiles()) {
            try {
                outposts.add(new Outpost(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static int createOutpost(McmeCommandSender sender) {
        Player player = ((SurvivalCommandSender)sender).getPlayer();
        Location loc = player.getLocation();
        for(Outpost outpost: outposts) {
            if (outpost.getLocation().distanceSquared(loc) < Outpost.getRadiusSquared() * 4) {
                sender.sendMessage(new ComponentBuilder("Too close to existing outpost!").color(ChatColor.RED).create());
                return 0;
            }
        }
        player.teleport(loc.clone().add(0,0,1));
        Outpost outpost = new Outpost(loc);
        outposts.add(outpost);
        sender.sendMessage(new ComponentBuilder("Outpost created!").color(ChatColor.GREEN).create());
        return 0;
    }

    public static int removeOutpost(McmeCommandSender sender) {
        Player player = ((SurvivalCommandSender)sender).getPlayer();
        Location loc = player.getLocation();
        Outpost removal = null;
        for(Outpost outpost: outposts) {
            if (outpost.getLocation().distanceSquared(loc) < Outpost.getRadiusSquared() * 4) {
                removal = outpost;
                break;
            }
        }
        if(removal != null) {
            outposts.remove(removal);
            sender.sendMessage(new ComponentBuilder("Outpost removed!").color(ChatColor.GREEN).create());
        } else {
            sender.sendMessage(new ComponentBuilder("No outpost in range!").color(ChatColor.RED).create());
        }
        return 0;
    }

    public static int saveTerrain(McmeCommandSender sender, String team) {
        Player player = ((SurvivalCommandSender)sender).getPlayer();
        Location loc = player.getLocation();
        for(Outpost outpost: outposts) {
            if (outpost.getLocation().distanceSquared(loc) < Outpost.getRadiusSquared() * 4) {
                outpost.saveTerrain(team);
                sender.sendMessage(new ComponentBuilder("Terrain saved!").color(ChatColor.GREEN).create());
                return 0;
            }
        }
        sender.sendMessage(new ComponentBuilder("No outpost in range!").color(ChatColor.RED).create());
        return 0;
    }

    public static World getWorld() {
        if(outposts.size()>0) {
            return outposts.get(0).getWorld();
        } else {
            return Bukkit.getWorlds().get(0);
        }
    }
}
