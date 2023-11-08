package net.guizhanss.gcereborn.setup;

import io.github.thebusybiscuit.slimefun4.api.researches.Research;

import net.guizhanss.gcereborn.GeneticChickengineering;
import net.guizhanss.gcereborn.items.GCEItems;
import net.guizhanss.gcereborn.utils.Keys;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class Researches {
    public static final Research MAIN = new Research(
        Keys.get("genetic_chickengineering"),
        29841,
        "Defying Nature",
        13
    );

    public static void setup() {
        MAIN.addItems(
            GCEItems.POCKET_CHICKEN,
            GCEItems.CHICKEN_NET,
            GCEItems.WATER_EGG,
            GCEItems.LAVA_EGG,
            GCEItems.GENETIC_SEQUENCER,
            GCEItems.EXCITATION_CHAMBER,
            GCEItems.EXCITATION_CHAMBER_2,
            GCEItems.EXCITATION_CHAMBER_3,
            GCEItems.PRIVATE_COOP
        );
        if (GeneticChickengineering.getConfigService().isPainEnabled()) {
            MAIN.addItems(GCEItems.RESTORATION_CHAMBER);
        }

        MAIN.register();
    }
}
