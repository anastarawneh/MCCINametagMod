package me.anastarawneh.mccinametagmod.nametags;

import me.anastarawneh.mccinametagmod.MCCINametagMod;
import me.anastarawneh.mccinametagmod.util.UnicodeChars;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class RocketSpleefRush {
    private static int HEALTH = 20;

    public static void setNametag(float health) {
        HEALTH = (int) health;
        setNametag();
    }
    
    public static void setNametag() {
        if (MCCINametagMod.STAGE.isEmpty() || MCCINametagMod.STAGE.equals("preRound")) {
            Nametags.TOP_LABEL = Text.empty();
            Nametags.BOTTOM_LABEL = Nametags.getStandardBottomText(false);
            Nametags.RANK_LABEL = Text.empty();
        }
        else if (MCCINametagMod.STAGE.equals("inRound")) {
            Nametags.TOP_LABEL = Text.literal(UnicodeChars.TeamFlagBig).setStyle(Style.EMPTY.withColor(MCCINametagMod.FACTION_COLOR).withFont(Identifier.of("mcc", "icon")))
                    .append(Text.literal(" ").setStyle(Style.EMPTY.withColor(Formatting.WHITE)))
                    .append(Nametags.getHealthBar(HEALTH, true));
            Nametags.BOTTOM_LABEL = Nametags.getStandardBottomText(false);
            Nametags.RANK_LABEL = Text.empty();
        }
        else if (MCCINametagMod.STAGE.equals("postGame")) {
            Nametags.TOP_LABEL = Text.empty();
            Nametags.BOTTOM_LABEL = Nametags.getStandardBottomText(false);
            Nametags.RANK_LABEL = Text.empty();
        }
        else {
            Nametags.TOP_LABEL = Text.empty();
            Nametags.BOTTOM_LABEL = Nametags.getStandardBottomText(true);
            Nametags.RANK_LABEL = Text.empty();
        }
    }
}
