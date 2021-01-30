package space.kiichan.geneticchickengineering.items;

import io.github.thebusybiscuit.slimefun4.core.attributes.MachineTier;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineType;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class GCEItems {

    private GCEItems() {}

    public static final SlimefunItemStack POCKET_CHICKEN = makeChicken("Pocket", "&7Right click on a block", "&7to release the chicken");
    public static final SlimefunItemStack CHICKEN_NET = new SlimefunItemStack("GCE_CHICKEN_NET", new ItemStack(Material.COBWEB), "Chicken Net", "Makes chickens portable", LoreBuilder.RIGHT_CLICK_TO_USE);
    public static final SlimefunItemStack WATER_EGG = new SlimefunItemStack("GCE_WATER_EGG", new ItemStack(Material.TURTLE_SPAWN_EGG), "&1Water Egg", "Contains water", LoreBuilder.RIGHT_CLICK_TO_USE);
    public static final SlimefunItemStack LAVA_EGG = new SlimefunItemStack("GCE_LAVA_EGG", new ItemStack(Material.STRIDER_SPAWN_EGG), "&cLava Egg", "Contains lava", LoreBuilder.RIGHT_CLICK_TO_USE);
    public static final SlimefunItemStack GENETIC_SEQUENCER = new SlimefunItemStack("GCE_GENETIC_SEQUENCER", new ItemStack(Material.SMOKER), "&cGenetic Sequencer", "Used to determine a chicken's genotype", LoreBuilder.machine(MachineTier.MEDIUM, MachineType.MACHINE), LoreBuilder.powerPerSecond(6));
    public static final SlimefunItemStack EXCITATION_CHAMBER = new SlimefunItemStack("GCE_EXCITATION_CHAMBER", new ItemStack(Material.BLAST_FURNACE), "&cExcitation Chamber", "Enables chickens to produce a resource", LoreBuilder.machine(MachineTier.MEDIUM, MachineType.MACHINE), LoreBuilder.powerPerSecond(10));
    public static final SlimefunItemStack EXCITATION_CHAMBER_2 = new SlimefunItemStack("GCE_EXCITATION_CHAMBER_2", new ItemStack(Material.BLAST_FURNACE), "&cBoosted Excitation Chamber", "Enables chickens to produce a resource, faster", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), LoreBuilder.powerPerSecond(15));
    public static final SlimefunItemStack PRIVATE_COOP = new SlimefunItemStack("GCE_PRIVATE_COOP", new ItemStack(Material.BEEHIVE), "&cPrivate Coop", "Big enough for two...", LoreBuilder.machine(MachineTier.MEDIUM, MachineType.MACHINE), LoreBuilder.powerPerSecond(2));

    public static SlimefunItemStack makeChicken(String chickenType, String... lore) {
        // Used only during plugin initialization
        String type = chickenType.replace(" ","_").toUpperCase();
        return new SlimefunItemStack("GCE_"+type+"_CHICKEN", "1638469a599ceef7207537603248a9ab11ff591fd378bea4735b346a7fae893", chickenType + " Chicken", lore);
    }

}
