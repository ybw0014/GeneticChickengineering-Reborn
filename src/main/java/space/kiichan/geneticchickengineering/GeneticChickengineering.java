package space.kiichan.geneticchickengineering;

import io.github.thebusybiscuit.cscorelib2.config.Config;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.updater.GitHubBuildsUpdater;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.core.researching.Research;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.UUID;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Chicken;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.World;
import space.kiichan.geneticchickengineering.chickens.ChickenTypes;
import space.kiichan.geneticchickengineering.chickens.PocketChicken;
import space.kiichan.geneticchickengineering.database.DBUtil;
import space.kiichan.geneticchickengineering.items.ChickenNet;
import space.kiichan.geneticchickengineering.items.GCEItems;
import space.kiichan.geneticchickengineering.items.ResourceEgg;
import space.kiichan.geneticchickengineering.listeners.WorldSavedListener;
import space.kiichan.geneticchickengineering.machines.ExcitationChamber;
import space.kiichan.geneticchickengineering.machines.GeneticSequencer;
import space.kiichan.geneticchickengineering.machines.PrivateCoop;

public class GeneticChickengineering extends JavaPlugin implements SlimefunAddon {

    private final NamespacedKey categoryId = new NamespacedKey(this, "genetic_chickengineering");
    private final NamespacedKey dnakey = new NamespacedKey(this, "gce_pocket_chicken_dna");;
    public PocketChicken pocketChicken;
    private Research research;
    public DBUtil db;
    public Logger log;

    @Override
    public void onEnable() {
        this.log = this.getLogger();
        this.db = new DBUtil(this.getDataFolder().toString(), this.log);
        if (!this.db.checkForConnection()) {
            this.log.severe("Connection to database failed. Aborting initialization.");
            this.log.severe("Check above for more information about the error.");
            return;
        }
        Config cfg = new Config(this);

        int mutationRate = clamp(1, cfg.getInt("options.mutation-rate"), 100, 30);
        int maxMutation = clamp(1, cfg.getInt("options.max-mutation"), 6, 2);
        boolean displayResources = cfg.getBoolean("options.display-resource-in-name");

        if (cfg.getBoolean("options.auto-update") && getDescription().getVersion().startsWith("DEV - ")) {
            new GitHubBuildsUpdater(this, getFile(), "kii-chan-reloaded/GeneticChickengineering/master").start();
        }

        SlimefunItemStack categoryIcon;

        try {
            categoryIcon = new SlimefunItemStack("GCE_ICON", "1638469a599ceef7207537603248a9ab11ff591fd378bea4735b346a7fae893", "&eGenetic Chickengineering", "", "&a> Click to open");
        } catch (Exception x) {
            categoryIcon = new SlimefunItemStack("GCE_ICON", Material.EGG, "&eGenetic Chickengineering", "", "&a> Click to open");
        }

        Category category = new Category(categoryId, categoryIcon);
        this.research = new Research(categoryId, 29841, "Defying Nature", 13);

        ItemStack[] nullRecipe = new ItemStack[] { null, null, null, null, null, null, null, null, null };

        this.pocketChicken = new PocketChicken(this, category, GCEItems.POCKET_CHICKEN, mutationRate, maxMutation, displayResources, dnakey, new RecipeType(new NamespacedKey(this, "gce_from_net"), new CustomItem(GCEItems.CHICKEN_NET,"Capture with a Chicken Net", "&for breed in a Private Coop")), nullRecipe);
        ChickenNet chickenNet = new ChickenNet(this, category, GCEItems.CHICKEN_NET, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
            null, new ItemStack(Material.STRING), new ItemStack(Material.STRING),
            null, new ItemStack(Material.STICK), new ItemStack(Material.STRING),
            null, new ItemStack(Material.STICK), null});
        GeneticSequencer geneticSequencer = new GeneticSequencer(this, category, GCEItems.GENETIC_SEQUENCER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
            new ItemStack(Material.OAK_PLANKS), null, new ItemStack(Material.OAK_PLANKS),
            new ItemStack(Material.COBBLESTONE), new ItemStack(Material.OBSERVER), new ItemStack(Material.COBBLESTONE),
            new ItemStack(Material.COBBLESTONE), SlimefunItems.ADVANCED_CIRCUIT_BOARD, new ItemStack(Material.COBBLESTONE)});
        ExcitationChamber excitationChamber = new ExcitationChamber(this, category, GCEItems.EXCITATION_CHAMBER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
            new ItemStack(Material.BLACKSTONE), SlimefunItems.SMALL_CAPACITOR, new ItemStack(Material.BLACKSTONE),
            new ItemStack(Material.CHAIN), null, new ItemStack(Material.CHAIN),
            new ItemStack(Material.STONE), SlimefunItems.ELECTRIC_MOTOR, new ItemStack(Material.STONE)});
        PrivateCoop privateCoop = new PrivateCoop(this, category, GCEItems.PRIVATE_COOP, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
            new ItemStack(Material.BIRCH_PLANKS), new ItemStack(Material.BIRCH_PLANKS), new ItemStack(Material.BIRCH_PLANKS),
            new ItemStack(Material.JUKEBOX), new ItemStack(Material.RED_BED), new ItemStack(Material.POPPY),
            new ItemStack(Material.BIRCH_PLANKS), SlimefunItems.HEATING_COIL, new ItemStack(Material.BIRCH_PLANKS)});

        RecipeType fromChicken = new RecipeType(new NamespacedKey(this, "gce_from_chicken"), new CustomItem(GCEItems.EXCITATION_CHAMBER,"Obtained from a Pocket Chicken", "in an Excitation Chamber"));

        SlimefunItem waterEgg = new ResourceEgg(this, category, GCEItems.WATER_EGG, Material.WATER, fromChicken);
        SlimefunItem lavaEgg = new ResourceEgg(this, category, GCEItems.LAVA_EGG, Material.LAVA, fromChicken);

        // Register items
        registerToAll(this.pocketChicken);
        registerToAll(chickenNet);
        registerToAll(waterEgg);
        registerToAll(lavaEgg);

        // Register machines
        registerToAll(geneticSequencer.setCapacity(180).setEnergyConsumption(6).setProcessingSpeed(1));
        registerToAll(privateCoop.setCapacity(30).setEnergyConsumption(3).setProcessingSpeed(1));
        registerToAll(excitationChamber.setCapacity(250).setEnergyConsumption(10).setProcessingSpeed(1));

        // Fill all resource chickens into the book
        ChickenTypes.registerChickens(research, this.pocketChicken, fromChicken);
        research.register();

        // Register listener to clean up database on world save
        new WorldSavedListener(this);

    }

    @Override
    public void onDisable() {
        this.cleanUpDB();
        this.db.close();
    }

    @Override
    public String getBugTrackerURL() {
        return "https://github.com/kii-chan-reloaded/GeneticChickengineering/issues";
    }

    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }


    private int clamp(int low, int value, int high, int fallback) {
        // Clamps an int between a minimum and maximum value
        // Fallback is unused
        return Math.min(Math.max(low, value), high);
    }
    private int clamp(int low, Object value, int high, int fallback) {
        // Clamps an int between a minimum and maximum value
        // Value is null, fallback is used instead
        return Math.min(Math.max(low, fallback), high);
    }

    public ItemStack convert(Chicken chick) {
        return this.pocketChicken.convert(chick);
    }

    private void registerToAll(SlimefunItem item) {
        item.register(this);
        this.research.addItems(item);
    }

    private void registerToAll(AContainer item) {
        item.register(this);
        this.research.addItems(item);
    }
    public void cleanUpDB() {
        List<String[]> chicks = this.db.getAll();
        if (chicks.size() == 0) {
            return;
        }
        List<World> ws = this.getServer().getWorlds();
        List<String> found = new ArrayList<String>();
        this.log.info("Starting database cleanup");
        for (int i=0; i<ws.size(); i++) {
            World w = ws.get(i);
            for (int j=0; j<chicks.size(); j++) {
                String uuid = chicks.get(j)[0];
                if (found.contains(uuid)) {
                    continue;
                }
                Entity chick = w.getEntity(UUID.fromString(uuid));
                if (chick != null) {
                    found.add(uuid);
                }
            }
        }
        int c = 0;
        for (int j=0; j<chicks.size(); j++) {
            String uuid = chicks.get(j)[0];
            if (found.contains(uuid)) {
                continue;
            }
            this.db.delete(uuid);
            c = c + 1;
        }
        this.db.commit();
        this.log.info(c+" old records deleted");
    }
}
