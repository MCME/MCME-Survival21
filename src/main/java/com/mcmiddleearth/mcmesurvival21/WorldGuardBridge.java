package com.mcmiddleearth.mcmesurvival21;

import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.mcmesurvival21.team.TeamManager;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class WorldGuardBridge {

    private static RegionManager regions;

    private final static String basePrefix = "base_";

    public static void init(World world) {
Logger.getLogger(WorldGuardBridge.class.getSimpleName()).info("init: "+world.getName());
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        regions = container.get(BukkitAdapter.adapt(world));
Logger.getLogger(WorldGuardBridge.class.getSimpleName()).info("Regions: "+String.join(" ",regions.getRegions().keySet()));
    }

    public static void addPlayerToTeam(Player player, String team) {
        ProtectedRegion region = regions.getRegion(basePrefix+team );
        if (region != null) {
            DefaultDomain members = region.getMembers();
            members.addPlayer(player.getUniqueId());
        }
    }

    public static void removePlayerFromTeam(Player player, String team) {
        ProtectedRegion region = regions.getRegion(basePrefix+team );
        if (region != null) {
            DefaultDomain members = region.getMembers();
            members.removePlayer(player.getUniqueId());
        }
    }

    public static int setProtection(McmeCommandSender sender, boolean enable) {
        if(enable) {
            for(String team: TeamManager.getTeamNames()) {
                ProtectedRegion region = regions.getRegion(basePrefix+team);
                if(region!=null) {
                    region.setFlag(Flags.ENTRY, StateFlag.State.DENY);
                    region.setFlag(Flags.ENTRY.getRegionGroupFlag(), RegionGroup.NON_MEMBERS);
                }
            }
            Bukkit.broadcast(new ComponentBuilder("Base Protection enabled!").bold(true).color(ChatColor.GREEN).create());
        } else {
            for(String team: TeamManager.getTeamNames()) {
                ProtectedRegion region = regions.getRegion(basePrefix + team);
                if(region!=null) {
                    region.setFlag(Flags.ENTRY, StateFlag.State.ALLOW);
                }
            }
            Bukkit.broadcast(new ComponentBuilder("Base Protection disabled!").bold(true).color(ChatColor.RED).create());
        }
        return 0;
    }
}
