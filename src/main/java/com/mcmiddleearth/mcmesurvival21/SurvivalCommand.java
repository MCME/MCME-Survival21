package com.mcmiddleearth.mcmesurvival21;

import com.google.common.base.Joiner;
import com.mcmiddleearth.command.AbstractCommandHandler;
import com.mcmiddleearth.command.SimpleTabCompleteRequest;
import com.mcmiddleearth.command.TabCompleteRequest;
import com.mcmiddleearth.command.builder.HelpfulLiteralBuilder;
import com.mcmiddleearth.command.builder.HelpfulRequiredArgumentBuilder;
import com.mcmiddleearth.mcmesurvival21.outpost.OutpostManager;
import com.mcmiddleearth.mcmesurvival21.team.TeamManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.mojang.brigadier.arguments.StringArgumentType.word;

public class SurvivalCommand extends AbstractCommandHandler implements TabExecutor {

    public SurvivalCommand(String command) {
        super(command);
    }

    @Override
    protected HelpfulLiteralBuilder createCommandTree(HelpfulLiteralBuilder commandNodeBuilder) {
        commandNodeBuilder
            .requires(sender -> ((SurvivalCommandSender)sender).hasPermission("survival.manager"))
            .then(HelpfulLiteralBuilder.literal("outpost")
                .requires(sender -> ((SurvivalCommandSender)sender).isPlayer())
                .then(HelpfulLiteralBuilder.literal("create")
                    .executes(context -> OutpostManager.createOutpost(context.getSource())))
                .then(HelpfulLiteralBuilder.literal("remove")
                    .executes(context -> OutpostManager.removeOutpost(context.getSource())))
                .then(HelpfulLiteralBuilder.literal("save")
                    .then(HelpfulRequiredArgumentBuilder.argument("team", word())
                        .executes(context -> OutpostManager.saveTerrain(context.getSource(),
                                                            context.getArgument("team", String.class))))))
            .then(HelpfulLiteralBuilder.literal("warp")
                .requires(sender ->((SurvivalCommandSender)sender).isPlayer())
                .then(HelpfulRequiredArgumentBuilder.argument("team",word())
                    .executes(context -> TeamManager.warpToBase(context.getSource(),
                                         context.getArgument("team", String.class)))))
            .then(HelpfulLiteralBuilder.literal("protection")
                    .then(HelpfulLiteralBuilder.literal("enable")
                            .executes(context -> WorldGuardBridge.setProtection(context.getSource(), true)))
                    .then(HelpfulLiteralBuilder.literal("disable")
                            .executes(context -> WorldGuardBridge.setProtection(context.getSource(), false))));
        return commandNodeBuilder;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        execute(new SurvivalCommandSender(commandSender), args);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        TabCompleteRequest request = new SimpleTabCompleteRequest(new SurvivalCommandSender(commandSender),
                String.format("/%s %s", label, Joiner.on(' ').join(args)).trim());
        onTabComplete(request);
        return request.getSuggestions();
    }
}
