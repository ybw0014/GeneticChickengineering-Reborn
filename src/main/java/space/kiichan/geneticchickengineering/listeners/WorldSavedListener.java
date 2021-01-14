package space.kiichan.geneticchickengineering.listeners;

import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import space.kiichan.geneticchickengineering.GeneticChickengineering;

public class WorldSavedListener implements Listener{
    private GeneticChickengineering plugin;
    
    public WorldSavedListener(GeneticChickengineering plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void onWorldSave(WorldSaveEvent e) {
        this.plugin.cleanUpDB();
    }
}
