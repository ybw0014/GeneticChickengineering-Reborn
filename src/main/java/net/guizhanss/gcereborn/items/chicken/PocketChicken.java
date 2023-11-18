package net.guizhanss.gcereborn.items.chicken;

import java.util.Optional;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Chicken;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.ItemUtils;

import net.guizhanss.gcereborn.GeneticChickengineering;
import net.guizhanss.gcereborn.core.adapters.AnimalsAdapter;
import net.guizhanss.gcereborn.core.genetics.DNA;
import net.guizhanss.gcereborn.utils.Keys;

public class PocketChicken extends SimpleSlimefunItem<ItemUseHandler> implements NotPlaceable {

    public static final AnimalsAdapter<Chicken> ADAPTER = new AnimalsAdapter<>(Chicken.class);

    public PocketChicken(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    @Nonnull
    public ItemUseHandler getItemHandler() {
        return e -> {
            e.cancel();

            Optional<Block> block = e.getClickedBlock();
            if (block.isEmpty()) {
                return;
            }

            Block b = block.get();
            Location location = b.getRelative(e.getClickedFace()).getLocation();
            Chicken entity = b.getWorld().spawn(location.toCenterLocation(), Chicken.class);

            ItemMeta meta = e.getItem().getItemMeta();
            JsonObject json = PersistentDataAPI.get(meta, Keys.ADAPTER, ADAPTER);
            ADAPTER.apply(entity, json);
            int[] dnaState = PersistentDataAPI.getIntArray(meta, Keys.DNA);
            DNA dna;
            if (dnaState != null) {
                dna = new DNA(dnaState);
            } else {
                dna = new DNA();
            }

            String dss = dna.getStateString();
            entity.setMetadata(Keys.METADATA, new FixedMetadataValue(GeneticChickengineering.getInstance(), dss));
            GeneticChickengineering.getDatabaseService().addChicken(entity.getUniqueId().toString(), dss);

            if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
                ItemUtils.consumeItem(e.getItem(), false);
            }

            if (GeneticChickengineering.getConfigService().isDisplayResources() && dna.isKnown()) {
                String name = "(" + ChickenTypes.getDisplayName(dna.getTyping()) + ")";
                if (json != null) {
                    if (!json.get("_customName").isJsonNull()) {
                        name = json.get("_customName").getAsString() + " " + name;
                    }
                    json.addProperty("_customNameVisible", true);
                    json.addProperty("_customName", name);
                    ADAPTER.apply(entity, json);
                } else {
                    entity.setCustomName(name);
                    entity.setCustomNameVisible(true);
                }
            }
        };
    }
}
