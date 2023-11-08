package net.guizhanss.gcereborn.core.commands.subcommands;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.guizhanss.gcereborn.core.commands.SubCommand;
import net.guizhanss.gcereborn.utils.DnaUtils;

public abstract class DnaSubCommand extends SubCommand {

    protected DnaSubCommand(@Nonnull String name, boolean hidden, @Nullable String permission, @Nonnull String usage) {
        super(name, hidden, permission, usage);
    }

    @Nonnull
    protected List<String> findDNA(@Nonnull String partial) {
        List<String> result = new LinkedList<>();
        for (String dna : DnaUtils.getPossibleDNA()) {
            if (dna.startsWith(partial)) {
                result.add(dna);
            }
        }
        return result;
    }
}
