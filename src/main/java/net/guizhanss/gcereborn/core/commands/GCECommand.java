package net.guizhanss.gcereborn.core.commands;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.guizhanss.gcereborn.GeneticChickengineering;
import net.guizhanss.gcereborn.core.commands.subcommands.CalcChanceCommand;
import net.guizhanss.gcereborn.core.commands.subcommands.MakeChickenCommand;
import net.guizhanss.guizhanlib.minecraft.utils.ChatUtil;

import lombok.Getter;

@Getter
public final class GCECommand implements CommandExecutor {
    private final Set<SubCommand> subCommands = new HashSet<>();

    public GCECommand() {
        var config = GeneticChickengineering.getConfigService();
        if (config.isSubCommandEnabled("makechicken")) {
            subCommands.add(new MakeChickenCommand());
        }
        if (config.isSubCommandEnabled("calcchance")) {
            subCommands.add(new CalcChanceCommand());
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            for (SubCommand subCommand : subCommands) {
                if (subCommand.isSubCommand(args[0])) {
                    subCommand.onCommand(sender, args);
                    return true;
                }
            }
            sendHelp(sender);
        } else {
            sendHelp(sender);
        }
        return true;
    }

    public void sendHelp(@Nonnull CommandSender sender) {
        sender.sendMessage(ChatUtil.color("&e&lGeneticChickengineering &6v" + GeneticChickengineering.getInstance().getPluginVersion()));
        for (SubCommand subCommand : subCommands) {
            if (subCommand.isHidden()) {
                continue;
            }
            sender.sendMessage(ChatUtil.color("&e/gce " + subCommand.getName() + "&7 - " + subCommand.getDescription()));
        }
    }
}
