package space.kiichan.geneticchickengineering.commands.subcommands;

import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import space.kiichan.geneticchickengineering.GeneticChickengineering;
import space.kiichan.geneticchickengineering.commands.Commands;

public abstract class Subcommand {

    public Commands commands;
    public GeneticChickengineering plugin;
    private String name;
    private String help;
    private Permission permission;

    protected Subcommand(GeneticChickengineering plugin, Commands commands, String permissionName) {
        this.plugin = plugin;
        this.commands = commands;
        this.permission = new Permission(permissionName);
    }

    public abstract boolean onExecute(CommandSender sender, String[] args);
    public abstract List<String> onTabComplete(CommandSender sender, String[] args);

    public String getName() {
        return this.name;
    }
    public String getHelp() {
        return this.help;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setHelp(String help) {
        this.help = help;
    }
    public boolean playerHasPermission(Player p) {
        return p.hasPermission(this.permission);
    }
}
