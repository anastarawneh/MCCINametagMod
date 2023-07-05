package me.anastarawneh.mccinametagmod.util;

import me.anastarawneh.mccinametagmod.MCCINametagMod;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

public class Util {
    public static TextColor GetFactionColor() {
        try {
            if ((MCCINametagMod.TEAM.toCharArray()[0] | 0x10000) >= (UnicodeChars.Red0.toCharArray()[0] | 0x10000) && (MCCINametagMod.TEAM.toCharArray()[0] | 0x10000) <= (UnicodeChars.Red9.toCharArray()[0] | 0x10000))
                return TextColor.fromRgb(16733524);
            else if ((MCCINametagMod.TEAM.toCharArray()[0] | 0x10000) >= (UnicodeChars.Orange0.toCharArray()[0] | 0x10000) && (MCCINametagMod.TEAM.toCharArray()[0] | 0x10000) <= (UnicodeChars.Orange9.toCharArray()[0] | 0x10000))
                return TextColor.fromFormatting(Formatting.GOLD);
            else if ((MCCINametagMod.TEAM.toCharArray()[0] | 0x10000) >= (UnicodeChars.Yellow0.toCharArray()[0] | 0x10000) && (MCCINametagMod.TEAM.toCharArray()[0] | 0x10000) <= (UnicodeChars.Yellow9.toCharArray()[0] | 0x10000))
                return TextColor.fromFormatting(Formatting.YELLOW);
            else if ((MCCINametagMod.TEAM.toCharArray()[0] | 0x10000) >= (UnicodeChars.Lime0.toCharArray()[0] | 0x10000) && (MCCINametagMod.TEAM.toCharArray()[0] | 0x10000) <= (UnicodeChars.Lime9.toCharArray()[0] | 0x10000))
                return TextColor.fromFormatting(Formatting.GREEN);
            else if ((MCCINametagMod.TEAM.toCharArray()[0] | 0x10000) >= (UnicodeChars.Green0.toCharArray()[0] | 0x10000) && (MCCINametagMod.TEAM.toCharArray()[0] | 0x10000) <= (UnicodeChars.Green9.toCharArray()[0] | 0x10000))
                return TextColor.fromFormatting(Formatting.DARK_GREEN);
            else if ((MCCINametagMod.TEAM.toCharArray()[0] | 0x10000) >= (UnicodeChars.Cyan0.toCharArray()[0] | 0x10000) && (MCCINametagMod.TEAM.toCharArray()[0] | 0x10000) <= (UnicodeChars.Cyan9.toCharArray()[0] | 0x10000))
                return TextColor.fromRgb(48025);
            else if ((MCCINametagMod.TEAM.toCharArray()[0] | 0x10000) >= (UnicodeChars.Aqua0.toCharArray()[0] | 0x10000) && (MCCINametagMod.TEAM.toCharArray()[0] | 0x10000) <= (UnicodeChars.Aqua9.toCharArray()[0] | 0x10000))
                return TextColor.fromRgb(5627391);
            else if ((MCCINametagMod.TEAM.toCharArray()[0] | 0x10000) >= (UnicodeChars.Blue0.toCharArray()[0] | 0x10000) && (MCCINametagMod.TEAM.toCharArray()[0] | 0x10000) <= (UnicodeChars.Blue9.toCharArray()[0] | 0x10000))
                return TextColor.fromRgb(5605631);
            else if ((MCCINametagMod.TEAM.toCharArray()[0] | 0x10000) >= (UnicodeChars.Purple0.toCharArray()[0] | 0x10000) && (MCCINametagMod.TEAM.toCharArray()[0] | 0x10000) <= (UnicodeChars.Purple9.toCharArray()[0] | 0x10000))
                return TextColor.fromRgb(8926207);
            else if ((MCCINametagMod.TEAM.toCharArray()[0] | 0x10000) >= (UnicodeChars.Pink0.toCharArray()[0] | 0x10000) && (MCCINametagMod.TEAM.toCharArray()[0] | 0x10000) <= (UnicodeChars.Pink9.toCharArray()[0] | 0x10000))
                return TextColor.fromFormatting(Formatting.LIGHT_PURPLE);
            else return TextColor.fromFormatting(Formatting.GRAY);
        } catch (Exception ex) {
            return TextColor.fromFormatting(Formatting.GRAY);
        }
    }
}
