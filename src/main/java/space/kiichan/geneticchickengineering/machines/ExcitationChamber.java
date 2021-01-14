package space.kiichan.geneticchickengineering.machines;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.cscorelib2.inventory.InvUtils;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import space.kiichan.geneticchickengineering.GeneticChickengineering;
import space.kiichan.geneticchickengineering.chickens.PocketChicken;

public class ExcitationChamber extends AContainer {
    private GeneticChickengineering plugin;
    private final PocketChicken pc;
    private ItemStack currentResource;

    public ExcitationChamber(GeneticChickengineering plugin, Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
        this.pc = plugin.pocketChicken;
        this.currentResource = new ItemStack(Material.AIR);
    }

    @Override
    public ItemStack getProgressBar() {
        return this.currentResource;
    }

    @Override
    public String getMachineIdentifier() {
        return "GCE_EXCITATION_CHAMBER";
    }

    @Override
    protected void tick(Block b) {
        super.tick(b);
        if (isProcessing(b)) {
            BlockMenu inv = BlockStorage.getInventory(b);
            if (this.findNextRecipe(inv) == null) {
                progress.remove(b);
                processing.remove(b);
                inv.replaceExistingItem(22, new CustomItem(Material.BLACK_STAINED_GLASS_PANE, " "));
                this.currentResource = new ItemStack(Material.AIR);
            }
        }
    }

    @Override
    protected MachineRecipe findNextRecipe(BlockMenu inv) {
        for (int slot : getInputSlots()) {
            ItemStack chick = inv.getItemInSlot(slot);

            if (chick == null) {
                continue;
            }

            if (this.pc.isPocketChicken(chick)) {
                if (!this.pc.isAdult(chick)) {
                    continue;
                }
                this.currentResource = this.pc.getResource(chick);
                MachineRecipe recipe = new MachineRecipe(8+2*this.pc.getResourceTier(chick), new ItemStack[] { chick }, new ItemStack[] {this.currentResource});
                // For some reason Maven doesn't believe that MachineRecipes have .getOutput
                if (!InvUtils.fitAll(inv.toInventory(), new ItemStack[] {this.currentResource}, getOutputSlots())) {
                    return null;
                }
                return recipe;
            }
        }

        return null;
    }

}
