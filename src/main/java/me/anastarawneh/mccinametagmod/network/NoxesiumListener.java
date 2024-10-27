package me.anastarawneh.mccinametagmod.network;

import com.noxcrew.noxesium.network.NoxesiumPackets;
import com.noxcrew.noxesium.network.clientbound.ClientboundMccGameStatePacket;
import com.noxcrew.noxesium.network.clientbound.ClientboundMccServerPacket;
import me.anastarawneh.mccinametagmod.MCCINametagMod;
import me.anastarawneh.mccinametagmod.nametags.BattleBox;
import me.anastarawneh.mccinametagmod.nametags.HoleInTheWall;
import me.anastarawneh.mccinametagmod.nametags.SkyBattle;
import me.anastarawneh.mccinametagmod.nametags.TGTTOS;
import me.anastarawneh.mccinametagmod.util.Game;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.text.Text;

import java.util.HashMap;

public class NoxesiumListener {
    public void init() {
        //? if >=1.21 {
            /*NoxesiumPackets.CLIENT_MCC_SERVER.addListener(this, (k, packet, context) -> handleMccServerPacket(packet, context));
            NoxesiumPackets.CLIENT_MCC_GAME_STATE.addListener(this, (k, packet, context) -> handleMccGameStatePacket(packet, context));
        *///?} else {
            NoxesiumPackets.MCC_SERVER.addListener(this, (k, packet, context) -> handleMccServerPacket(packet, context));
            NoxesiumPackets.MCC_GAME_STATE.addListener(this, (k, packet, context) -> handleMccGameStatePacket(packet, context));
        //?}
    }

    private void handleMccServerPacket(ClientboundMccServerPacket packet, ClientPlayNetworking.Context context) {
        if (MCCINametagMod.getConfig().debug) {
            HashMap<String, String> map = new HashMap<>();
            map.put("serverType", packet.serverType());
            map.put("subType", packet.subType());
            map.put("associatedGame", packet.associatedGame());
            context.player().sendMessage(Text.literal("Received packet -> " + map), false);
        }

        switch (packet.serverType()) {
            case "hole-in-the-wall":
                MCCINametagMod.GAME = Game.HOLE_IN_THE_WALL;
                HoleInTheWall.setNametag();
                break;
            case "tgttos":
                MCCINametagMod.GAME = Game.TGTTOS;
                TGTTOS.setNametag();
                break;
            case "battle-box":
                MCCINametagMod.GAME = Game.BATTLE_BOX;
                MCCINametagMod.STAGE = "";
                BattleBox.setNametag();
                break;
            case "sky-battle":
                MCCINametagMod.GAME = Game.SKY_BATTLE;
                MCCINametagMod.STAGE = "";
                SkyBattle.setNametag();
                break;
            case "parkour-warrior":
                if (packet.subType().equals("main")) MCCINametagMod.GAME = Game.PARKOUR_WARRIOR_DOJO;
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
                if (packet.subType().equals("parkour-warrior")) MCCINametagMod.GAME = Game.PARKOUR_WARRIOR_LOBBY;
                else MCCINametagMod.GAME = Game.NONE;
                break;
        }
    }

    private void handleMccGameStatePacket(ClientboundMccGameStatePacket packet, ClientPlayNetworking.Context context) {
        if (MCCINametagMod.getConfig().debug) {
            HashMap<String, String> map = new HashMap<>();
            map.put("phaseType", packet.phaseType());
            map.put("stage", packet.stage());
            map.put("round", String.valueOf(packet.round()));
            map.put("totalRounds", String.valueOf(packet.totalRounds()));
            map.put("mapId", packet.mapId());
            map.put("mapName", packet.mapName());
            context.player().sendMessage(Text.literal("Received packet -> " + map), false);
        }

        MCCINametagMod.STAGE = packet.stage();
        MCCINametagMod.PHASE_TYPE = packet.phaseType();
    }
}
