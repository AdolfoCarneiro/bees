package com.curiousbees.common.content.builtin;

import com.curiousbees.common.genetics.mutation.MutationDefinition;
import com.curiousbees.common.genetics.mutation.MutationResultMode;

import java.util.List;

import static com.curiousbees.common.content.builtin.BuiltinBeeSpecies.*;

/**
 * Centralized built-in mutation definitions for the two MVP species mutations.
 *
 * Note: the spec defines a 95% PARTIAL / 5% FULL split per mutation.
 * The current MutationDefinition model supports one result mode per definition.
 * For now, both mutations use PARTIAL at their full base chance.
 * Follow-up task: extend MutationDefinition or MutationService to support weighted result modes.
 */
public final class BuiltinBeeMutations {

    private BuiltinBeeMutations() {}

    /** Meadow + Forest -> Cultivated at 12% base chance. */
    public static final MutationDefinition CULTIVATED_FROM_MEADOW_FOREST = new MutationDefinition(
            "curious_bees:mutations/cultivated_from_meadow_forest",
            SPECIES_MEADOW.id(),
            SPECIES_FOREST.id(),
            SPECIES_CULTIVATED,
            0.12,
            MutationResultMode.PARTIAL);

    /** Forest + Arid -> Hardy at 8% base chance. */
    public static final MutationDefinition HARDY_FROM_FOREST_ARID = new MutationDefinition(
            "curious_bees:mutations/hardy_from_forest_arid",
            SPECIES_FOREST.id(),
            SPECIES_ARID.id(),
            SPECIES_HARDY,
            0.08,
            MutationResultMode.PARTIAL);

    /** All MVP mutation definitions in order. Pass this list to MutationService. */
    public static final List<MutationDefinition> ALL = List.of(
            CULTIVATED_FROM_MEADOW_FOREST,
            HARDY_FROM_FOREST_ARID);
}
