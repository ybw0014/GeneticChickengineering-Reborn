package net.guizhanss.gcereborn.core.commands;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;

import net.guizhanss.gcereborn.GeneticChickengineering;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
public abstract class SubCommand {

    private final String name;
    @Accessors(fluent = true)
    private final boolean isHidden;
    private final String permission;
    private final String usage;

    protected SubCommand(@Nonnull String name, boolean hidden, @Nullable String permission, @Nonnull String usage) {
        this.name = name;
        this.isHidden = hidden;
        this.permission = permission;
        this.usage = usage;
    }

    public boolean isSubCommand(@Nonnull String cmd) {
        return name.equalsIgnoreCase(cmd);
    }

    @Nonnull
    public String getDescription() {
        return GeneticChickengineering.getLocalization().getString("commands." + name + ".description");
    }

    @ParametersAreNonnullByDefault
    public abstract void onCommand(CommandSender sender, String[] args);

    @ParametersAreNonnullByDefault
    protected boolean canExecute(CommandSender sender, String[] args) {
        if (permission != null && !sender.hasPermission(permission)) {
            GeneticChickengineering.getLocalization().sendMessage(sender, "no-permission");
            return false;
        }
        String[] usageArgs = usage.split(" ");
        int argsLength = 1;
        for (String usageArg : usageArgs) {
            if (usageArg.startsWith("[")) {
                break;
            }
            argsLength++;
        }
        if (args.length != argsLength) {
            GeneticChickengineering.getLocalization().sendMessage(sender, "usage",
                "/gce " + getName() + " " + usage);
            return false;
        }
        return true;
    }

    @Nullable
    @ParametersAreNonnullByDefault
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return List.of();
    }
}
