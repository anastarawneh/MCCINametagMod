package me.anastarawneh.mccinametagmod.mixin;

import me.anastarawneh.mccinametagmod.MCCINametagMod;
import me.anastarawneh.mccinametagmod.util.Util;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
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
                    MCCINametagMod.FACTION_COLOR = Util.GetFactionColor();
                    break;
                }
            }
        }
    }
}
