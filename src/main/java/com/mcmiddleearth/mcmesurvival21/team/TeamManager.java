package com.mcmiddleearth.mcmesurvival21.team;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

public class TeamManager {

    private static Map<String, Team> teams = new HashMap<>();

    public TeamManager() {
        teams.put("human",new Team("human",Material.BLUE_STAINED_GLASS));
        teams.put("elf",new Team("elf",Material.GREEN_STAINED_GLASS));
        teams.put("dwarf",new Team("dwarf",Material.RED_STAINED_GLASS));
        teams.put("orc",new Team("orc",Material.BLACK_STAINED_GLASS));
    }

    public static ArrayList<Player> getHumanMembers() { return teams.get("human").getMembers(); }

    public static ArrayList<Player> getElfMembers() {
        return teams.get("elf").getMembers();
    }

    public static ArrayList<Player> getDwarfMembers() {
        return teams.get("dwarf").getMembers();
    }

    public static ArrayList<Player> getOrcMembers() {
        return teams.get("orc").getMembers();
    }

    public static Team getTeam(Player player) {
        for (Team team : teams.values()) {
            if (team.getMembers().contains(player)) {
                return team;
            }
        }
        return null;
    }

    public static Set<String> getTeamNames() {
        return teams.keySet();
    }
}
