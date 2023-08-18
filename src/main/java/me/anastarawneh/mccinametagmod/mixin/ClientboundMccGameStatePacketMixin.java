package me.anastarawneh.mccinametagmod.mixin;

import com.noxcrew.noxesium.network.clientbound.ClientboundMccGameStatePacket;
import me.anastarawneh.mccinametagmod.MCCINametagMod;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientboundMccGameStatePacket.class)
public class ClientboundMccGameStatePacketMixin {
    @Shadow @Final public String stage;

    @Shadow @Final public String phaseType;

    @Inject(method = "receive", at = @At("HEAD"))
    private void receive(ClientPlayerEntity player, PacketSender responseSender, CallbackInfo ci) {
        MCCINametagMod.STAGE = this.stage;
        MCCINametagMod.PHASE_TYPE = this.phaseType;
    }
}
