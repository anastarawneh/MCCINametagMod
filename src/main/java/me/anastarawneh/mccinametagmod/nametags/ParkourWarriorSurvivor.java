package me.anastarawneh.mccinametagmod.nametags;

import me.anastarawneh.mccinametagmod.MCCINametagMod;
import me.anastarawneh.mccinametagmod.util.UnicodeChars;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class ParkourWarriorSurvivor {
    private static int HEALTH = 10;
    private static int MAX_HEALTH = 10;

    public static void setNametag(float health, float maxHealth) {
        health /= 2;
        HEALTH = (int) health;
        maxHealth /= 2;
        MAX_HEALTH = (int) maxHealth;
        setNametag();
    }
    
    public static void setNametag() {
        String playerName = MinecraftClient.getInstance().player.getName().getString();

        if (!MCCINametagMod.PHASE_TYPE.equals("POST_GAME")) {
            Nametags.TOP_LABEL = Text.literal(UnicodeChars.TeamFlagBig).setStyle(Style.EMPTY.withColor(MCCINametagMod.FACTION_COLOR).withFont(new Identifier("mcc:icon")))
                    .append(Text.literal(" ").setStyle(Style.EMPTY.withColor(Formatting.WHITE)))
                    .append(Nametags.getPartialHealthBar(HEALTH, MAX_HEALTH));Nametags.BOTTOM_LABEL = Text.literal(playerName).setStyle(Style.EMPTY.withColor(MCCINametagMod.FACTION_COLOR).withFont(new Identifier("minecraft:default")));
            Nametags.RANK_LABEL = Text.empty();
        }
        else if (MCCINametagMod.STAGE.equals("postGame")) {
            Nametags.TOP_LABEL = Text.empty();
            Nametags.BOTTOM_LABEL = Text.literal(playerName).setStyle(Style.EMPTY.withColor(MCCINametagMod.FACTION_COLOR));
            Nametags.RANK_LABEL = Text.empty();
        }
        else {
            Nametags.TOP_LABEL = Text.empty();
            Nametags.BOTTOM_LABEL = Nametags.getStandardBottomText(false);
            Nametags.RANK_LABEL = Text.empty();
        }
    }
}
