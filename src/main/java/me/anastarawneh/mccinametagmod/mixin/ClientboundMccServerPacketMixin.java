package me.anastarawneh.mccinametagmod.mixin;

import com.noxcrew.noxesium.network.clientbound.ClientboundMccServerPacket;
import me.anastarawneh.mccinametagmod.MCCINametagMod;
import me.anastarawneh.mccinametagmod.util.Game;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientboundMccServerPacket.class)
public class ClientboundMccServerPacketMixin {
    @Shadow(remap = false) @Final public String type;

    @Shadow(remap = false) @Final public String subType;

    @Inject(method = "receive", at = @At("HEAD"))
    private void receive(ClientPlayerEntity player, PacketSender responseSender, CallbackInfo ci) {
        switch (this.type) {
            case "hole-in-the-wall":
                MCCINametagMod.GAME = Game.HOLE_IN_THE_WALL;
                break;
            case "tgttos":
                MCCINametagMod.GAME = Game.TGTTOS;
                break;
            case "battle-box":
                MCCINametagMod.GAME = Game.BATTLE_BOX;
                MCCINametagMod.STAGE = "";
                MCCINametagMod.PHASE_TYPE = "";
                break;
            case "sky-battle":
                MCCINametagMod.GAME = Game.SKY_BATTLE;
                MCCINametagMod.STAGE = "";
                MCCINametagMod.PHASE_TYPE = "";
                break;
            case "parkour-warrior":
                if (subType.equals("main")) MCCINametagMod.GAME = Game.PARKOUR_WARRIOR_DOJO;
                else {
                    MCCINametagMod.GAME = Game.PARKOUR_WARRIOR_SURVIVOR;
                    MCCINametagMod.STAGE = "";
                    MCCINametagMod.PHASE_TYPE = "";
                }
                break;
            case "dynaball":
                MCCINametagMod.GAME = Game.DYNABALL;
                MCCINametagMod.STAGE = "";
                MCCINametagMod.PHASE_TYPE = "";
                break;
            case "rocket-spleef":
                MCCINametagMod.GAME = Game.ROCKET_SPLEEF_RUSH;
                MCCINametagMod.STAGE = "";
                MCCINametagMod.PHASE_TYPE = "";
                break;
            default:
                if (subType.equals("parkour-warrior")) MCCINametagMod.GAME = Game.PARKOUR_WARRIOR_LOBBY;
                else MCCINametagMod.GAME = Game.NONE;
                break;
        }
    }
}
