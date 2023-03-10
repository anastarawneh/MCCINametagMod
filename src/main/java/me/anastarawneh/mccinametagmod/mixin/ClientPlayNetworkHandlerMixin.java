package me.anastarawneh.mccinametagmod.mixin;

import me.anastarawneh.mccinametagmod.MCCINametagMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
    @Shadow public abstract Collection<PlayerListEntry> getPlayerList();

    @Inject(method = "onPlayerList(Lnet/minecraft/network/packet/s2c/play/PlayerListS2CPacket;)V", at = @At("TAIL"))
    public void onPlayerList(PlayerListS2CPacket packet, CallbackInfo ci) {
        if (MCCINametagMod.TEAM.equals("")) {
            Collection<PlayerListEntry> tabList = getPlayerList();
            for (PlayerListEntry tabEntry : tabList) {
                if (tabEntry.getDisplayName() != null && tabEntry.getDisplayName().getSiblings().get(3).getString().equals(MinecraftClient.getInstance().getSession().getUsername())) {
                    MCCINametagMod.TEAM = tabEntry.getDisplayName().getSiblings().get(1).getString();
                    MCCINametagMod.RANK = tabEntry.getDisplayName().getSiblings().get(0).getString();
                    MCCINametagMod.COLOR = tabEntry.getDisplayName().getSiblings().get(3).getStyle().getColor();
                    break;
                }
            }
        }
    }

    @Inject(method = "onGameMessage(Lnet/minecraft/network/packet/s2c/play/GameMessageS2CPacket;)V", at = @At("TAIL"))
    public void onGameMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        if (packet.content().getString().contains("[\uE06F] Battle Box")||
                packet.content().getString().contains("[\uE076] Sky Battle")) {
            MCCINametagMod.GAME_STAGE = 0;
        }
        if (packet.content().getString().contains("[\uE0B6] You have chosen the") ||
                packet.content().getString().contains("[\uE076] Game started!")) {
            MCCINametagMod.GAME_STAGE = 1;
        }
        if (packet.content().getString().contains("(\uE022) You receive: Battle Box Tokens") ||
                packet.content().getString().contains("[\uE076] Game Over!")) {
            MCCINametagMod.GAME_STAGE = 2;
        }
    }
}
