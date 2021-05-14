package com.mcmiddleearth.mcmesurvival21.outpost;

import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class OutpostListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        OutpostManager.getOutposts().forEach(outpost -> {
            if(outpost.getLocation().getBlock().equals(event.getBlock())
                || outpost.getLocation().getBlock().equals(event.getBlock().getRelative(BlockFace.DOWN))) {
                event.setCancelled(true);
            }
        });
    }
}
