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
    protected void tick(Block b) {
        super.tick(b);
        if (isProcessing(b)) {
            if (Math.random() < 0.25) {
                Location l = b.getLocation().toCenterLocation();
                l.getWorld().spawnParticle(Particle.HEART, l.add(0,0.5,0), 2, 0.2, 0, 0.2);
            }
            BlockMenu inv = BlockStorage.getInventory(b);
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

            // Until my patch for CSCoreLib goes through, this is insuffient.

            //~ // Without this, the inventory tends to stack pocket chickens, therefore overriding their dna
            //~ Inventory onestackinv = inv.toInventory();
            //~ onestackinv.setMaxStackSize(1);
            //~ // For some reason Maven doesn't believe that MachineRecipes have .getOutput
            //~ if (!InvUtils.fitAll(onestackinv, new ItemStack[] {baby}, getOutputSlots())) {
                //~ return null;
            //~ }

            // This isn't necessarily better, but it's only what I need
             Inventory invi = inv.toInventory();
             for (int slot : getOutputSlots()) {
                ItemStack stack = invi.getItem(slot);

                if (stack == null || stack.getType() == Material.AIR) {
                    return recipe;
                }
            }

            //~ return recipe;
        }
        return null;
    }

}
