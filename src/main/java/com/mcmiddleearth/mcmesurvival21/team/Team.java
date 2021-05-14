package com.mcmiddleearth.mcmesurvival21.team;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Team {

    private final String name;

    private final ArrayList<Player> members = new ArrayList<>();

    private final Material beaconMaterial;

    public Team(String name, Material beaconMaterial) {
        this.name = name;
        this.beaconMaterial = beaconMaterial;
    }

    public ArrayList<Player> getMembers() {
        return members;
    }

    public Material getBeaconMaterial() {
        return beaconMaterial;
    }

    public String getName() {
        return name;
    }
}
