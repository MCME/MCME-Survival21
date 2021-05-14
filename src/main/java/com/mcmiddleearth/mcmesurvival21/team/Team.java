package com.mcmiddleearth.mcmesurvival21.team;

import com.mcmiddleearth.mcmesurvival21.MCMESurvival21;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Team {

    private final String name;

    private final ArrayList<UUID> members = new ArrayList<>();

    private final org.bukkit.scoreboard.Team scoreboardTeam;

    private Material beaconMaterial;

    private final Location spawn;

    private ChatColor color;

    private final static File teamFolder = new File(MCMESurvival21.getPlugin(MCMESurvival21.class).getDataFolder(),"teams");
    private final File file;
    private final static String fileExtension = ".yml";


    static {
        if(!teamFolder.exists()) {
            teamFolder.mkdirs();
        }
    }

    public Team(File file, Scoreboard scoreboard) throws IOException, InvalidConfigurationException {
        this.file = file;
        this.name = file.getName().substring(0,file.getName().lastIndexOf('.'));
        YamlConfiguration config = new YamlConfiguration();
        config.load(file);
        try {
            beaconMaterial = Material.valueOf(config.getString("beaconMaterial","WHITE_STAINED_GLASS").toUpperCase());
        } catch(IllegalArgumentException ex) {
            beaconMaterial = Material.WHITE_STAINED_GLASS;
        }
        try {
            color = ChatColor.valueOf(config.getString("color", "WHITE").toUpperCase());
        } catch(IllegalArgumentException ex) {
            color = ChatColor.WHITE;
        }
        scoreboardTeam = scoreboard.registerNewTeam(name);
        scoreboardTeam.setPrefix(""+color);
        scoreboardTeam.setColor(color);
        ConfigurationSection section = config.getConfigurationSection("spawn");
            spawn = new Location(Bukkit.getWorld(section.getString("world","survival")),
                                section.getDouble("x",0),
                                section.getDouble("y",100),
                                section.getDouble("z",0),
                                (float) section.getDouble("pitch",0),
                                (float) section.getDouble("yaw",0));
        List<String> players = config.getStringList("members");
        players.forEach(player-> members.add(UUID.fromString(player)));
    }

    /*public Team(String name, Location spawn, Material beaconMaterial, Scoreboard scoreboard) {
        this.name = name;
        this.spawn = spawn;
        this.beaconMaterial = beaconMaterial;
        scoreboardTeam = scoreboard.registerNewTeam(name);
        scoreboardTeam.set
        file = new File(teamFolder,name+fileExtension);
        saveToFile();
    }*/

    public static File getTeamFolder() {
        return teamFolder;
    }

    public ArrayList<UUID> getMembers() {
        return members;
    }

    public Material getBeaconMaterial() {
        return beaconMaterial;
    }

    public Location getSpawn() {
        return spawn;
    }

    public String getName() {
        return name;
    }

    public void add(Player player) {
        members.add(player.getUniqueId());
        saveToFile();
    }

    public void remove(Player player) {
        members.remove(player.getUniqueId());
        saveToFile();
    }

    public org.bukkit.scoreboard.Team getScoreboardTeam() {
        return scoreboardTeam;
    }

    private void saveToFile() {
        YamlConfiguration config = new YamlConfiguration();
        config.set("beaconMaterial",beaconMaterial.name());
        config.set("color", color.name());
        ConfigurationSection section = config.createSection("spawn");
            section.set("world",spawn.getWorld().getName());
            section.set("x",spawn.getX());
            section.set("y",spawn.getY());
            section.set("z",spawn.getZ());
            section.set("pitch",spawn.getPitch());
            section.set("yaw", spawn.getYaw());
        List<String> players = new ArrayList<>();
        members.forEach(player-> players.add(player.toString()));
        config.set("members", players);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
