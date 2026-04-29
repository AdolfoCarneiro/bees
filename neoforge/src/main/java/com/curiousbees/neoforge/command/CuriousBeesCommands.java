package com.curiousbees.neoforge.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
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
                    .then(Commands.literal("inspect_apiary")
                        .executes(DebugInspectApiaryCommand::execute))
                    .then(Commands.literal("set_bee_genome")
                        .then(Commands.argument("species", StringArgumentType.word())
                            .suggests(DebugSetGenomeCommand.SUGGESTIONS)
                            .executes(DebugSetGenomeCommand::execute)))
                    .then(Commands.literal("roll_production")
                        .executes(DebugProductionRollCommand::execute))
                )
        );
    }
}
