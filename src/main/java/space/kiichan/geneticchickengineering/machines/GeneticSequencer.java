package space.kiichan.geneticchickengineering.machines;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import io.github.thebusybiscuit.cscorelib2.inventory.InvUtils;
import org.bukkit.inventory.ItemStack;
import space.kiichan.geneticchickengineering.GeneticChickengineering;
import space.kiichan.geneticchickengineering.chickens.PocketChicken;
import space.kiichan.geneticchickengineering.items.GCEItems;

public class GeneticSequencer extends AContainer {
    private GeneticChickengineering plugin;
    private final PocketChicken pc;

    public GeneticSequencer(GeneticChickengineering plugin, Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
        this.pc = plugin.pocketChicken;
    }

    @Override
    public ItemStack getProgressBar() {
        return GCEItems.POCKET_CHICKEN;
    }

    @Override
    public String getMachineIdentifier() {
        return "GCE_GENETIC_SEQUENCER";
    }

    @Override
    protected MachineRecipe findNextRecipe(BlockMenu inv) {
        for (int slot : getInputSlots()) {
            ItemStack item = inv.getItemInSlot(slot);

            if (item == null) {
                continue;
            }
            ItemStack chick = item.clone();
            // Just in case these got stacked somehow
            chick.setAmount(1);

            if (this.pc.isPocketChicken(chick)) {
                if (this.pc.isLearned(chick)) {
                    continue;
                }
                ItemStack learnedChick = this.pc.learnDNA(chick);
                MachineRecipe recipe = new MachineRecipe(30, new ItemStack[] { chick }, new ItemStack[] {learnedChick});
                if (!InvUtils.fitAll(inv.toInventory(), recipe.getOutput(), getOutputSlots())) {
                    continue;
                }
                inv.consumeItem(slot, 1);

                return recipe;
            }
        }

        return null;
    }

}
