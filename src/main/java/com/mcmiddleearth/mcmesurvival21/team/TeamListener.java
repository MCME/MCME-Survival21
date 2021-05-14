package com.mcmiddleearth.mcmesurvival21.team;

import com.mcmiddleearth.mcmesurvival21.MCMESurvival21;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class TeamListener implements Listener {

//    CommandDispatcher<Player> dispatcher = new CommandDispatcher<>();

    //private String messageString = "";

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
    /*
        if(!TeamManager.getHumanMembers().contains(e.getPlayer())&&!TeamManager.getElfMembers().contains(e.getPlayer())
                &&!TeamManager.getDwarfMembers().contains(e.getPlayer())&&!TeamManager.getOrcMembers().contains(e.getPlayer())){
            Random num = new Random();
            int selection = num.nextInt(4);
            switch(selection) {
                case (0) : Human.addPlayer(e.getPlayer());
                case (1) : Elf.addPlayer(e.getPlayer());
                case (2) : Dwarf.addPlayer(e.getPlayer());
                case (3) : Orc.addPlayer(e.getPlayer());
            }
        }
    }

    public void addToTeam(Player p, String team) {
        switch (team) {
            case "Human":
                if(!TeamManager.getHumanMembers().contains(p)) {
                    Human.addPlayer(p);
                    TeamManager.getHumanMembers().add(p);
                }
            case "Elf":
                if(!TeamManager.getElfMembers().contains(p)) {
                    Elf.addPlayer(p);
                    TeamManager.getElfMembers().add(p);
                }
            case "Dwarf":
                if(!TeamManager.getDwarfMembers().contains(p)) {
                    Dwarf.addPlayer(p);
                    TeamManager.getDwarfMembers().add(p);
                }
            case "Orc":
                if(!TeamManager.getOrcMembers().contains(p)) {
                    Orc.addPlayer(p);
                    TeamManager.getOrcMembers().add(p);
                }
        }
    }

    public void OnCommand(PlayerCommandSendEvent e){
        dispatcher.register(LiteralArgumentBuilder.<Player>literal("s")
                .then(LiteralArgumentBuilder.<Player>literal("join")
                        .then(LiteralArgumentBuilder.<Player>literal("Human")
                                .executes(c->{
                                    addToTeam(e.getPlayer(), "Human");
                                    return 1;
                                }))
                        .then(LiteralArgumentBuilder.<Player>literal("Dwarf")
                                .executes(c->{
                                    addToTeam(e.getPlayer(), "Dwarf");
                                    return 1;
                                }))
                        .then(LiteralArgumentBuilder.<Player>literal("Elf")
                                .executes(c->{
                                    addToTeam(e.getPlayer(), "Elf");
                                    return 1;
                                }))
                        .then(LiteralArgumentBuilder.<Player>literal("Orc")
                                .executes(c->{
                                    addToTeam(e.getPlayer(), "Orc");
                                    return 1;
                                }))
                ));
    }

    public boolean onCommand(CommandSender cs, Command cmnd, String label, String[] args) {

        if(args.length >= 1 && cs instanceof Player) {

            Player p = (Player) cs;

            if(!TeamManager.getHumanMembers().contains(p) &&
                    !TeamManager.getElfMembers().contains(p) &&
                    !TeamManager.getDwarfMembers().contains(p) &&
                    !TeamManager.getOrcMembers().contains(p)){
                p.sendMessage(ChatColor.RED + "You aren't on a team!");
            }

            for(String str : args) {

                if(messageString.equals("")) {
                    messageString += str;
                }

                else if(!str.equals("")) {
                    messageString += (" " + str);
                }

                else {
                    return false;
                }

            }
            if(TeamManager.getHumanMembers().contains(p)){
                for(Player player : TeamManager.getHumanMembers()){

                    player.sendMessage(ChatColor.DARK_RED + "[TEAM] " + p.getName() + ChatColor.RED + ": " + messageString);

                }
            }
            else if(TeamManager.getElfMembers().contains(p)){
                for(Player player : TeamManager.getElfMembers()){

                    player.sendMessage(ChatColor.BLUE + "[TEAM] " + p.getName() + ChatColor.AQUA + ": " + messageString);

                }
            }

            else if(TeamManager.getDwarfMembers().contains(p)){
                for(Player player : TeamManager.getDwarfMembers()){

                    player.sendMessage(ChatColor.DARK_RED + "[TEAM] " + p.getName() + ChatColor.RED + ": " + messageString );

                }
            }

            else if(TeamManager.getOrcMembers().contains(p)){
                for(Player player : TeamManager.getOrcMembers()){

                    player.sendMessage(ChatColor.BLUE + "[TEAM] " + p.getName() + ChatColor.AQUA + ": " + messageString);

                }
            }

            messageString = "";
            return true;
        }else{
            return false;
        }

    }*/

}

