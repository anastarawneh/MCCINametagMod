package me.anastarawneh.mccinametagmod.command;

import com.mojang.brigadier.context.CommandContext;
import me.anastarawneh.mccinametagmod.MCCINametagMod;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class LogExceptionCommand {
    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(literal("logexception")
                .executes(LogExceptionCommand::execute)));
    }

    static int execute(CommandContext<FabricClientCommandSource> context) {
        if (MCCINametagMod.LATEST_EXCEPTION == null) {
            context.getSource().sendFeedback(MCCINametagMod.MESSAGE_PREFIX.copy().append("Nothing to debug."));
            return 1;
        }
        MCCINametagMod.LOGGER.warn("Throwing latest exception:", MCCINametagMod.LATEST_EXCEPTION);
        context.getSource().sendFeedback(MCCINametagMod.MESSAGE_PREFIX.copy().append("Logged the latest exception."));
        MCCINametagMod.LATEST_EXCEPTION = null;
        return 1;
    }
}
