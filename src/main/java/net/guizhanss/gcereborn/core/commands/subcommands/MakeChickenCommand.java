package net.guizhanss.gcereborn.core.commands.subcommands;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.guizhanss.gcereborn.GeneticChickengineering;
import net.guizhanss.gcereborn.core.genetics.DNA;
import net.guizhanss.gcereborn.utils.PocketChickenUtils;
import net.guizhanss.guizhanlib.minecraft.utils.InventoryUtil;

public class MakeChickenCommand extends DnaSubCommand {

    public MakeChickenCommand() {
        super("makechicken", false, "geneticchickengineering.command.makechicken", "<DNA>");
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onCommand(CommandSender sender, String[] args) {
        if (!canExecute(sender, args)) {
            return;
        }
        if (!(sender instanceof Player p)) {
            GeneticChickengineering.getLocalization().sendMessage(sender, "no-console");
            return;
        }

        String notation = args[0];
        if (!DNA.isValidSequence(notation)) {
            GeneticChickengineering.getLocalization().sendMessage(sender, "invalid-dna-notation", notation);
            return;
        }

        DNA dna = new DNA(notation.toCharArray());
        ItemStack chicken = PocketChickenUtils.fromDNA(dna);
        InventoryUtil.push(p, chicken);
    }

    @Override
    @ParametersAreNonnullByDefault
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> result = new LinkedList<>();
        if (args.length == 1) {
            result = findDNA(args[0]);
        }
        return result;
    }
}
