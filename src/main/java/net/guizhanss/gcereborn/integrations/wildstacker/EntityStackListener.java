package net.guizhanss.gcereborn.integrations.wildstacker;

import com.bgsoftware.wildstacker.api.events.EntityStackEvent;

import org.bukkit.entity.Chicken;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.guizhanss.gcereborn.GeneticChickengineering;

public class EntityStackListener implements Listener {

    public EntityStackListener(GeneticChickengineering plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityStack(EntityStackEvent e) {
        if (e.getEntity().getType() != EntityType.CHICKEN || e.getTarget().getType() != EntityType.CHICKEN) {
            return;
        }
        var source = (Chicken) e.getEntity().getLivingEntity();
        var target = (Chicken) e.getTarget().getLivingEntity();
        var dbService = GeneticChickengineering.getDatabaseService();

        GeneticChickengineering.debug("source: " + source.getUniqueId() + ", has dna: " + dbService.hasChicken(source.getUniqueId()));
        GeneticChickengineering.debug("target: " + target.getUniqueId() + ", has dna: " + dbService.hasChicken(target.getUniqueId()));

        // both have no dna data, safe to merge.
        if (!dbService.hasChicken(source.getUniqueId()) && !dbService.hasChicken(target.getUniqueId())) {
            return;
        }

        // one of them has dna data, cancel merging.
        if (dbService.hasChicken(source.getUniqueId()) != dbService.hasChicken(target.getUniqueId())) {
            e.setCancelled(true);
            return;
        }

        // now both have dna data, check if they are different.
        // if so, cancel merging.
        if (!dbService.getChickenDNA(source.getUniqueId()).equals(dbService.getChickenDNA(target.getUniqueId()))) {
            e.setCancelled(true);
        }
    }
}
