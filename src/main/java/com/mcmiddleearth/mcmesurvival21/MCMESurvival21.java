package com.mcmiddleearth.mcmesurvival21;

import com.mcmiddleearth.mcmesurvival21.outpost.Outpost;
import com.mcmiddleearth.mcmesurvival21.outpost.OutpostListener;
import com.mcmiddleearth.mcmesurvival21.outpost.OutpostManager;
import com.mcmiddleearth.mcmesurvival21.outpost.OutpostUpdater;
import com.mcmiddleearth.mcmesurvival21.team.TeamListener;
import com.mcmiddleearth.mcmesurvival21.team.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public final class MCMESurvival21 extends JavaPlugin {

    BukkitTask outpostUpdater;

    @Override
    public void onEnable() {
        // Plugin startup logic
        TeamManager.init();
        OutpostManager.init();
        WorldGuardBridge.init(OutpostManager.getWorld());
        Bukkit.getPluginManager().registerEvents(new OutpostListener(), this);
        Bukkit.getPluginManager().registerEvents(new TeamListener(), this);
        outpostUpdater = new OutpostUpdater().runTaskTimer(this, 500, Outpost.getUpdateInterval());
        SurvivalCommand survivalCommandHandler = new SurvivalCommand("survival");
        PluginCommand survivalCommand = Bukkit.getServer().getPluginCommand("survival");
        if(survivalCommand!=null) {
            survivalCommand.setTabCompleter(survivalCommandHandler);
            survivalCommand.setExecutor(survivalCommandHandler);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if(outpostUpdater!=null) {
            outpostUpdater.cancel();
        }
    }
}
