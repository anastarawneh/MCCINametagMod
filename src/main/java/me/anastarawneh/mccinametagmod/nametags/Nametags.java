package me.anastarawneh.mccinametagmod.nametags;

import me.anastarawneh.mccinametagmod.MCCINametagMod;
import me.anastarawneh.mccinametagmod.util.UnicodeChars;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class Nametags {
    public static MutableText TOP_LABEL = Text.empty();
    public static MutableText BOTTOM_LABEL = Text.empty();
    public static MutableText RANK_LABEL = Text.empty();

    public static MutableText getStandardBottomText(boolean faction) {
        String playerName = MinecraftClient.getInstance().player.getName().getString();

        if (faction) return Text.literal(MCCINametagMod.RANK + MCCINametagMod.FACTION + MCCINametagMod.CROWN).setStyle(Style.EMPTY.withColor(MCCINametagMod.COLOR).withFont(Identifier.of("mcc", "icon")))
                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(Identifier.of("mcc", "padding_nosplit"))))
                .append(Text.literal(playerName).setStyle(Style.EMPTY.withFont(Identifier.of("minecraft", "default"))));
        else return Text.literal(MCCINametagMod.RANK + MCCINametagMod.CROWN).setStyle(Style.EMPTY.withColor(MCCINametagMod.COLOR).withFont(Identifier.of("mcc", "icon")))
                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(Identifier.of("mcc", "padding_nosplit"))))
                .append(Text.literal(playerName).setStyle(Style.EMPTY.withFont(Identifier.of("minecraft", "default"))));
    }

    public static MutableText getHealthBar(int health) {
        return getHealthBar(health, false);
    }

    public static MutableText getHealthBar(int health, boolean hideNumber) {
        MutableText label = Text.empty();
        for (int segment = 0; segment < 5; segment++) {
            if (health >= 4 + (4 * segment)) label = label.append(Text.literal(UnicodeChars.HealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(Identifier.of("mcc", "icon"))));
            else if (health == 3 + (4 * segment)) label = label.append(Text.literal(UnicodeChars.HealthBar1_5).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(Identifier.of("mcc", "icon"))));
            else if (health == 2 + (4 * segment)) label = label.append(Text.literal(UnicodeChars.HealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(Identifier.of("mcc", "icon"))));
            else if (health == 1 + (4 * segment)) label = label.append(Text.literal(UnicodeChars.HealthBar0_5).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(Identifier.of("mcc", "icon"))));
            else if (segment == 4 && !hideNumber) {
                label = label.append(Text.literal(switch (health) {
                    case 16 -> UnicodeChars.HealthNum8_0;
                    case 15 -> UnicodeChars.HealthNum7_5;
                    case 14 -> UnicodeChars.HealthNum7_0;
                    case 13 -> UnicodeChars.HealthNum6_5;
                    case 12 -> UnicodeChars.HealthNum6_0;
                    case 11 -> UnicodeChars.HealthNum5_5;
                    case 10 -> UnicodeChars.HealthNum5_0;
                    case  9 -> UnicodeChars.HealthNum4_5;
                    case  8 -> UnicodeChars.HealthNum4_0;
                    case  7 -> UnicodeChars.HealthNum3_5;
                    case  6 -> UnicodeChars.HealthNum3_0;
                    case  5 -> UnicodeChars.HealthNum2_5;
                    case  4 -> UnicodeChars.HealthNum2_0;
                    case  3 -> UnicodeChars.HealthNum1_5;
                    case  2 -> UnicodeChars.HealthNum1_0;
                    case  1 -> UnicodeChars.HealthNum0_5;
                    default -> UnicodeChars.HealthBar0_0;
                }).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(Identifier.of("mcc", "icon"))));
            }
            else label = label.append(Text.literal(UnicodeChars.HealthBar0_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(Identifier.of("mcc", "icon"))));

            if (segment < 4) label = label.append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(Identifier.of("mcc", "negative_padding_nosplit"))));
        }
        return label;
    }

    public static MutableText getPartialHealthBar(int health, int maxHealth) {
        MutableText label = Text.empty();
        health /= 2;
        maxHealth /= 2;
        for (int segment = 0; segment < 5; segment++) {
            if (health >= 2 + (2 * segment)) label = label.append(Text.literal(UnicodeChars.HealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(Identifier.of("mcc", "icon"))));
            else if (maxHealth == 1 + (2 * segment) && health == 1 + (2 * segment)) label = label.append(Text.literal(UnicodeChars.DisabledHealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(Identifier.of("mcc", "icon"))));
            else if (maxHealth == 1 + (2 * segment)) label = label.append(Text.literal(UnicodeChars.DisabledHealthBar1_0Hit).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(Identifier.of("mcc", "icon"))));
            else if (health == 1 + (2 * segment)) label = label.append(Text.literal(UnicodeChars.HealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(Identifier.of("mcc", "icon"))));
            else if (maxHealth < (2 * segment)) label = label.append(Text.literal(UnicodeChars.DisabledHealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(Identifier.of("mcc", "icon"))));
            else label = label.append(Text.literal(UnicodeChars.HealthBar0_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(Identifier.of("mcc", "icon"))));

            if (segment < 4) label = label.append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(Identifier.of("mcc", "negative_padding_nosplit"))));
        }
        return label;
    }
}
