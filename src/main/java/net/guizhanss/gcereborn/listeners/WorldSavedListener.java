package net.guizhanss.gcereborn.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;

import net.guizhanss.gcereborn.GeneticChickengineering;

public class WorldSavedListener implements Listener {

    public WorldSavedListener(GeneticChickengineering plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onWorldSave(WorldSaveEvent e) {
        GeneticChickengineering.getDatabaseService().cleanup();
    }
}
