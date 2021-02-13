package space.kiichan.geneticchickengineering.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import space.kiichan.geneticchickengineering.GeneticChickengineering;
import space.kiichan.geneticchickengineering.commands.Commands;
import space.kiichan.geneticchickengineering.commands.subcommands.Subcommand;

public class Executor implements CommandExecutor {
    Commands commands;
    GeneticChickengineering plugin;

    public Executor(GeneticChickengineering plugin, Commands commands) {
        this.commands = commands;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Available subcommands:");
            for (String name: this.commands.getSubcommands()) {
                sender.sendMessage("    "+name);
            }
            return false;
        }
        Subcommand subcommand = this.commands.getSubcommand(args[0]);
        if (subcommand != null) {
            String[] subargs = new String[args.length-1];
            for (int i=0; i < args.length-1; i++) {
                subargs[i] = args[i+1];
            }
            return subcommand.onExecute(sender, subargs);
        } else {
            sender.sendMessage("Invalid subcommand: "+args[0]);
        }
        return false;
    }
}
