package com.mcmiddleearth.mcmesurvival21.team;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class TeamManager {

    private final static Map<String, Team> teams = new HashMap<>();

    private final static Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

    private final static Random random = new Random();

    public static void init() {
        //teams.put("human",new Team("human",Material.BLUE_STAINED_GLASS, scoreboard));
        //teams.put("elf",new Team("elf",Material.GREEN_STAINED_GLASS, scoreboard));
        //teams.put("dwarf",new Team("dwarf",Material.RED_STAINED_GLASS, scoreboard));
        //teams.put("orc",new Team("orc",Material.BLACK_STAINED_GLASS, scoreboard));
        for(File file: Team.getTeamFolder().listFiles()) {
            try {
                teams.put(file.getName(), new Team(file, scoreboard));
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
    }

    /*public static ArrayList<Player> getOnlineHumanMembers() { return getOnlineMembers("human"); }

    public static ArrayList<Player> getOnlineElfMembers() {
        return getOnlineMembers("elf");
    }

    public static ArrayList<Player> getOnlineDwarfMembers() {
        return getOnlineMembers("dwarf");
    }

    public static ArrayList<Player> getOnlineOrcMembers() {
        return getOnlineMembers("orc");
    }*/


    public static ArrayList<Player> getOnlineMembers(String team) {
        ArrayList<Player> result = new ArrayList<>();
        ArrayList<UUID> members = teams.get(team).getMembers();
        members.forEach(member -> {
            Player player = Bukkit.getPlayer(member);
            if(player!=null) {
                result.add(player);
            }
        });
        return result;
    }

    public static Team getTeam(Player player) {
        for (Team team : teams.values()) {
            if (team.getMembers().contains(player.getUniqueId())) {
                return team;
            }
        }
        return null;
    }

    public static Team getRandomTeam() {
        Set<String> teamNames = TeamManager.getTeamNames();
        int teamIndex = random.nextInt(teamNames.size());
        Iterator<String> it = teamNames.iterator();
        for(int i = 0; i < teamIndex; i++) {
            it.next();
        }
        return teams.get(it.next());
    }

    public static void addToTeam(Player player, String teamName) {
        Team team = teams.get(teamName);
        if(team !=null) {
            team.add(player);
        }
    }

    public static void removeFrom(Player player, String teamName) {
        Team team = teams.get(teamName);
        if(team !=null) {
            team.remove(player);
        }
    }


    public static Set<String> getTeamNames() {
        return teams.keySet();
    }
}
