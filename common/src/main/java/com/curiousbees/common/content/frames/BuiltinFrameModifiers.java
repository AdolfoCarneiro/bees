package com.curiousbees.common.content.frames;

import com.curiousbees.common.gameplay.frames.FrameModifier;

import java.util.Map;

/**
 * Built-in frame modifier definitions for Phase 7F.
 */
public final class BuiltinFrameModifiers {

    private BuiltinFrameModifiers() {}

    public static final FrameModifier BASIC = new FrameModifier(
            "curiousbees:basic_frame",
            1.03,
            1.03,
            1);

    public static final FrameModifier MUTATION = new FrameModifier(
            "curiousbees:mutation_frame",
            1.18,
            1.00,
            1);

    public static final FrameModifier PRODUCTIVITY = new FrameModifier(
            "curiousbees:productivity_frame",
            1.00,
            1.18,
            1);

    public static final Map<String, FrameModifier> BY_ID = Map.of(
            BASIC.id(), BASIC,
            MUTATION.id(), MUTATION,
            PRODUCTIVITY.id(), PRODUCTIVITY);
}
