package com.mcmiddleearth.mcmesurvival21.team;

import com.mcmiddleearth.command.McmeCommandSender;
import com.mcmiddleearth.mcmesurvival21.SurvivalCommandSender;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class TeamManager {

    private final static Map<String, Team> teams = new HashMap<>();

    private final static Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

    private final static Random random = new Random();

    public static void init() {
        for(File file: Team.getTeamFolder().listFiles()) {
            try {
                teams.put(file.getName().substring(0,file.getName().lastIndexOf('.')), new Team(file, scoreboard));
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
    }

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

    public static int warpToBase(McmeCommandSender sender, String teamName) {
        Player player = ((SurvivalCommandSender)sender).getPlayer();
        Team team = teams.get(teamName);
        if(team != null) {
            player.teleport(team.getSpawn());
            sender.sendMessage(new ComponentBuilder("Teleported to "+teamName+" base!").color(ChatColor.GREEN).create());
        } else {
            sender.sendMessage(new ComponentBuilder("Team not found!").color(ChatColor.RED).create());
        }
        return 0;
    }
}
