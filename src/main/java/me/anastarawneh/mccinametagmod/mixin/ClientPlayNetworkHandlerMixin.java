package me.anastarawneh.mccinametagmod.mixin;

import me.anastarawneh.mccinametagmod.MCCINametagMod;
import me.anastarawneh.mccinametagmod.util.Util;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
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
        try {
            if (MCCINametagMod.FACTION.equals("")) {
                Collection<PlayerListEntry> tabList = getPlayerList();
                for (PlayerListEntry tabEntry : tabList) {
                    if (tabEntry.getDisplayName() != null && tabEntry.getDisplayName().getSiblings().get(4).getString().equals(MinecraftClient.getInstance().getSession().getUsername())) {
                        MCCINametagMod.RANK = tabEntry.getDisplayName().getSiblings().get(0).getString();
                        MCCINametagMod.FACTION = tabEntry.getDisplayName().getSiblings().get(1).getString();
                        MCCINametagMod.CROWN = tabEntry.getDisplayName().getSiblings().get(2).getString();
                        MCCINametagMod.COLOR = tabEntry.getDisplayName().getSiblings().get(4).getStyle().getColor();
                        break;
                    }
                }
            }

            if (MCCINametagMod.FACTION_COLOR == TextColor.fromFormatting(Formatting.GRAY)) {
                MCCINametagMod.FACTION_COLOR = Util.GetFactionColor();
            }
        } catch (IndexOutOfBoundsException ignored) {

        }
    }
}
