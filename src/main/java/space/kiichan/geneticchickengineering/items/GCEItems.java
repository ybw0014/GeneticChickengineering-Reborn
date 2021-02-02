package space.kiichan.geneticchickengineering.items;

import io.github.thebusybiscuit.slimefun4.core.attributes.MachineTier;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineType;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class GCEItems {

    private GCEItems() {}

    public static final SlimefunItemStack POCKET_CHICKEN = new SlimefunItemStack("GCE_POCKET_CHICKEN", "1638469a599ceef7207537603248a9ab11ff591fd378bea4735b346a7fae893", "§r§bPocket Chicken", "§7Right click on a block", "§7to release the chicken");
    public static final SlimefunItemStack CHICKEN_NET = new SlimefunItemStack("GCE_CHICKEN_NET", new ItemStack(Material.COBWEB), "§aChicken Net", "§r§7§oMakes chickens portable", LoreBuilder.RIGHT_CLICK_TO_USE);
    public static final SlimefunItemStack WATER_EGG = new SlimefunItemStack("GCE_WATER_EGG", new ItemStack(Material.TURTLE_SPAWN_EGG), "§9Water Egg", "§r§7§oContains water", LoreBuilder.RIGHT_CLICK_TO_USE);
    public static final SlimefunItemStack LAVA_EGG = new SlimefunItemStack("GCE_LAVA_EGG", new ItemStack(Material.STRIDER_SPAWN_EGG), "§cLava Egg", "§r§7§oContains lava", LoreBuilder.RIGHT_CLICK_TO_USE);
    public static final SlimefunItemStack GENETIC_SEQUENCER = new SlimefunItemStack("GCE_GENETIC_SEQUENCER", new ItemStack(Material.SMOKER), "§eGenetic Sequencer", "§r§7§oUsed to determine a chicken's genotype", LoreBuilder.machine(MachineTier.MEDIUM, MachineType.MACHINE), LoreBuilder.powerPerSecond(6));
    public static final SlimefunItemStack EXCITATION_CHAMBER = new SlimefunItemStack("GCE_EXCITATION_CHAMBER", new ItemStack(Material.BLAST_FURNACE), "§eExcitation Chamber", "§r§7§oEnables chickens to produce a resource", LoreBuilder.machine(MachineTier.MEDIUM, MachineType.MACHINE), LoreBuilder.powerPerSecond(10));
    public static final SlimefunItemStack EXCITATION_CHAMBER_2 = new SlimefunItemStack("GCE_EXCITATION_CHAMBER_2", new ItemStack(Material.BLAST_FURNACE), "§eBoosted Excitation Chamber", "§r§7§oEnables chickens to produce a resource,", "§r§7§obut faster", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), LoreBuilder.powerPerSecond(15));
    public static final SlimefunItemStack PRIVATE_COOP = new SlimefunItemStack("GCE_PRIVATE_COOP", new ItemStack(Material.BEEHIVE), "§ePrivate Coop", "§r§7§oBig enough for two...", LoreBuilder.machine(MachineTier.MEDIUM, MachineType.MACHINE), LoreBuilder.powerPerSecond(2));

}
