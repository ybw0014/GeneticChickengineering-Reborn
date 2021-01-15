package space.kiichan.geneticchickengineering.machines;

import java.util.LinkedList;
import java.util.List;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.cscorelib2.inventory.InvUtils;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import space.kiichan.geneticchickengineering.GeneticChickengineering;
import space.kiichan.geneticchickengineering.chickens.PocketChicken;
import space.kiichan.geneticchickengineering.items.GCEItems;

public class PrivateCoop extends AContainer {
    private GeneticChickengineering plugin;
    private final PocketChicken pc;
    private int[] dynamicOutputSlots;
    private final int[] staticOutputSlots = new int[]{ 24, 25 };

    public PrivateCoop(GeneticChickengineering plugin, Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
        this.pc = plugin.pocketChicken;
    }

    @Override
    public ItemStack getProgressBar() {
        return GCEItems.POCKET_CHICKEN;
    }

    @Override
    public String getMachineIdentifier() {
        return "GCE_PRIVATE_COOP";
    }

    @Override
    public int[] getOutputSlots() {
        if (this.dynamicOutputSlots == null) {
            return new int[]{ 24, 25 };
        } else {
            return this.dynamicOutputSlots;
        }
    }

    @Override
    protected void tick(Block b) {
        super.tick(b);
        if (isProcessing(b)) {
            if (Math.random() < 0.25) {
                Location l = b.getLocation().toCenterLocation();
                l.getWorld().spawnParticle(Particle.HEART, l.add(0,0.5,0), 2, 0.2, 0, 0.2);
            }
            BlockMenu inv = BlockStorage.getInventory(b);
            // Check if parent chickens have been removed
            if (this.findNextRecipe(inv) == null) {
                progress.remove(b);
                processing.remove(b);
                inv.replaceExistingItem(22, new CustomItem(Material.BLACK_STAINED_GLASS_PANE, " "));
            }
        }
    }

    @Override
    protected MachineRecipe findNextRecipe(BlockMenu inv) {
        List<ItemStack> parents = new LinkedList<ItemStack>();
        this.dynamicOutputSlots = new int[]{ 24, 25 };
        for (int slot : getInputSlots()) {
            ItemStack parent = inv.getItemInSlot(slot);
            if (parent == null) {
                return null;
            }
            if (this.pc.isPocketChicken(parent)) {
                if (this.pc.isAdult(parent)) {
                    parents.add(parent);
                }
            }
        }
        if (parents.size() == 2) {
            ItemStack baby = this.pc.breed(parents.get(0), parents.get(1));
            if (baby == null) {
                // Shouldn't ever be here, just in case
                return null;
            }
            MachineRecipe recipe = new MachineRecipe(120, new ItemStack[] { parents.get(0), parents.get(1) }, new ItemStack[] {baby});

            /* Here we circumvent baby chicks being stacked, therefore making a
             * clone of the elder sibling instead of a new baby.
             * 
             * There are patches incoming to CS-CoreLib to fix this, but for
             * the time being this remains an issue.
             * 
             * Commented out below is the preferred method to do this once the
             * changes get merged. Also remove the static/dynamicOutputSlots
             * and the getOutputSlots override.
             */
            Inventory invi = inv.toInventory();
            for (int slot : this.staticOutputSlots) {
                ItemStack stack = invi.getItem(slot);

                if (stack == null || stack.getType() == Material.AIR) {
                    this.dynamicOutputSlots = new int[]{ slot };
                    return recipe;
                }
            }

            //~ Inventory invi = inv.toInventory();
            //~ invi.setMaxStackSize(1);
            //~ if (!InvUtils.fits(invi, baby, getOutputSlots())) {
                //~ return null;
            //~ }

            //~ return recipe;
        }
        return null;
    }

}
