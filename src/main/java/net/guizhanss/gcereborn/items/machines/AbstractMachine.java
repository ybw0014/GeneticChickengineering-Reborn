package net.guizhanss.gcereborn.items.machines;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;

public abstract class AbstractMachine extends AContainer {

    protected static final int INFO_SLOT = 22;

    private final String identifier;

    @ParametersAreNonnullByDefault
    protected AbstractMachine(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        identifier = item.getItemId();
    }

    @Override
    @Nonnull
    public String getMachineIdentifier() {
        return identifier;
    }
}
