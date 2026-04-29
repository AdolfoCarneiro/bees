package com.curiousbees.neoforge.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

/** Registers the full /curiousbees command tree. */
public final class CuriousBeesCommands {

    private CuriousBeesCommands() {}

    public static void register(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(
            Commands.literal("curiousbees")
                .then(Commands.literal("debug")
                    .requires(src -> src.hasPermission(2))
                    .then(Commands.literal("inspect_bee")
                        .executes(DebugInspectCommand::execute))
                )
        );
    }
}
