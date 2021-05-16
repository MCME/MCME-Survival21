package com.mcmiddleearth.mcmesurvival21.team;

import com.mcmiddleearth.mcmesurvival21.MCMESurvival21;
import com.mcmiddleearth.mcmesurvival21.WorldGuardBridge;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class TeamListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Team team = TeamManager.getTeam(player);
        if (team == null && !player.hasPermission("survival.exempt")) {
            team = TeamManager.getRandomTeam();
            team.add(player);
            player.teleport(team.getSpawn());
        }
        if(team != null) {
            team.getScoreboardTeam().addEntry(player.getName());
            player.addAttachment(MCMESurvival21.getPlugin(MCMESurvival21.class), "venturechat."+team.getName()+"channel", true);
            player.setBedSpawnLocation(team.getSpawn(), true);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        Team team = TeamManager.getTeam(player);
        if (team != null) {
            team.getScoreboardTeam().removeEntry(player.getName());
        }
    }

}

