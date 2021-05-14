package com.mcmiddleearth.mcmesurvival21;

import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.mcmesurvival21.team.TeamManager;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class WorldGuardBridge {

    private static RegionManager regions;

    private final static String baseSuffix = "_base";

    public static void init(World world) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        regions = container.get(BukkitAdapter.adapt(world));
    }

    public static void addPlayerToTeam(Player player, String team) {
        ProtectedRegion region = regions.getRegion(team + baseSuffix);
        if (region != null) {
            DefaultDomain members = region.getMembers();
            members.addPlayer(player.getUniqueId());
        }
    }

    public static int setProtection(McmeCommandSender sender, boolean enable) {
        if(enable) {
            for(String team: TeamManager.getTeamNames()) {
                ProtectedRegion region = regions.getRegion(team + baseSuffix);
                if(region!=null) {
                    region.setFlag(Flags.ENTRY.getRegionGroupFlag(), RegionGroup.MEMBERS);
                }
            }
            Bukkit.broadcast(new ComponentBuilder("Base Protection enabled!").bold(true).color(ChatColor.GREEN).create());
        } else {
            for(String team: TeamManager.getTeamNames()) {
                ProtectedRegion region = regions.getRegion(team + baseSuffix);
                if(region!=null) {
                    region.setFlag(Flags.ENTRY.getRegionGroupFlag(), RegionGroup.ALL);
                }
            }
            Bukkit.broadcast(new ComponentBuilder("Base Protection disabled!").bold(true).color(ChatColor.RED).create());
        }
        return 0;
    }
}
