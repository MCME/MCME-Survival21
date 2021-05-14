package com.mcmiddleearth.mcmesurvival21.outpost;

import org.bukkit.scheduler.BukkitRunnable;

public class OutpostUpdater extends BukkitRunnable {

    @Override
    public void run() {
        OutpostManager.getOutposts().forEach(Outpost::checkOwner);
    }
}
