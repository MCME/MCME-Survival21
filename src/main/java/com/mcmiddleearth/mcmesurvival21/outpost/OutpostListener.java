package com.mcmiddleearth.mcmesurvival21.outpost;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class OutpostListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        OutpostManager.getOutposts().forEach(outpost -> {
            Block outpostBlock = outpost.getLocation().getBlock();
            if(outpostBlock.equals(event.getBlock())
                || outpostBlock.equals(event.getBlock().getRelative(BlockFace.DOWN))
                    || outpostBlock.equals(event.getBlock().getRelative(-1,1,-1))
                    || outpostBlock.equals(event.getBlock().getRelative(-1,1,0))
                    || outpostBlock.equals(event.getBlock().getRelative(-1,1,1))
                    || outpostBlock.equals(event.getBlock().getRelative(0,1,-1))
                    || outpostBlock.equals(event.getBlock().getRelative(0,1,0))
                    || outpostBlock.equals(event.getBlock().getRelative(0,1,1))
                    || outpostBlock.equals(event.getBlock().getRelative(1,1,-1))
                    || outpostBlock.equals(event.getBlock().getRelative(1,1,0))
                    || outpostBlock.equals(event.getBlock().getRelative(1,1,1))
            ) {
                event.setCancelled(true);
            }
        });
    }
}
