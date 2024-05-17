package me.anastarawneh.mccinametagmod.nametags;

import me.anastarawneh.mccinametagmod.MCCINametagMod;
import net.minecraft.text.Text;

public class TGTTOS {
    public static void setNametag() {
        if (!MCCINametagMod.STAGE.equals("podiumPhase")) {
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
