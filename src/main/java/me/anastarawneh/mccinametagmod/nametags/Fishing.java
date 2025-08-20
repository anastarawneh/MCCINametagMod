package me.anastarawneh.mccinametagmod.nametags;

import me.anastarawneh.mccinametagmod.MCCINametagMod;
import me.anastarawneh.mccinametagmod.mixin.IngameHudAccessor;
import me.anastarawneh.mccinametagmod.util.UnicodeChars;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class Fishing {
    public static void setNametag() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        String playerName = player.getName().getString();

        Text actionBar = ((IngameHudAccessor) MinecraftClient.getInstance().inGameHud).getOverlayMessage();
        String fishingGlyphBig = actionBar.getSiblings().get(0).getSiblings().get(13).getString();
        String fishingGlyph = "?";
        if (fishingGlyphBig.equals(UnicodeChars.FishingLevel0Big)) fishingGlyph = UnicodeChars.FishingLevel0;
        else if (fishingGlyphBig.equals(UnicodeChars.FishingLevel1Big)) fishingGlyph = UnicodeChars.FishingLevel1;
        else if (fishingGlyphBig.equals(UnicodeChars.FishingLevel2Big)) fishingGlyph = UnicodeChars.FishingLevel2;
        else if (fishingGlyphBig.equals(UnicodeChars.FishingLevel3Big)) fishingGlyph = UnicodeChars.FishingLevel3;
        else if (fishingGlyphBig.equals(UnicodeChars.FishingLevel4Big)) fishingGlyph = UnicodeChars.FishingLevel4;
        else if (fishingGlyphBig.equals(UnicodeChars.FishingLevel5Big)) fishingGlyph = UnicodeChars.FishingLevel5;
        else if (fishingGlyphBig.equals(UnicodeChars.FishingLevel6Big)) fishingGlyph = UnicodeChars.FishingLevel6;
        else if (fishingGlyphBig.equals(UnicodeChars.FishingLevel7Big)) fishingGlyph = UnicodeChars.FishingLevel7;
        else if (fishingGlyphBig.equals(UnicodeChars.FishingLevel8Big)) fishingGlyph = UnicodeChars.FishingLevel8;
        else if (fishingGlyphBig.equals(UnicodeChars.FishingLevel9Big)) fishingGlyph = UnicodeChars.FishingLevel9;
        else if (fishingGlyphBig.equals(UnicodeChars.FishingLevel10Big)) fishingGlyph = UnicodeChars.FishingLevel10;

        Nametags.TOP_LABEL = Text.literal(playerName).setStyle(Style.EMPTY.withColor(MCCINametagMod.COLOR));
        int crownLevel = player.experienceLevel;
        int fishingLevel = Integer.parseInt(actionBar.getSiblings().get(0).getSiblings().get(15).getString());
        Nametags.BOTTOM_LABEL = Text.literal(MCCINametagMod.FACTION).setStyle(Style.EMPTY.withFont(Identifier.of("mcc", "icon")))
                .append(Text.literal(MCCINametagMod.FACTION_LEVEL + " ").setStyle(Style.EMPTY.withFont(Identifier.of("minecraft", "default"))))
                .append(Text.literal(MCCINametagMod.CROWN).setStyle(Style.EMPTY.withFont(Identifier.of("mcc", "icon"))))
                .append(Text.literal(crownLevel + " ").setStyle(Style.EMPTY.withColor(Formatting.YELLOW).withFont(Identifier.of("minecraft", "default"))))
                .append(Text.literal(fishingGlyph).setStyle(Style.EMPTY.withFont(Identifier.of("mcc", "icon"))))
                .append(Text.literal(String.valueOf(fishingLevel)).setStyle(Style.EMPTY.withColor(Formatting.AQUA).withFont(Identifier.of("minecraft", "default"))));
        Nametags.RANK_LABEL = Text.literal(MCCINametagMod.RANK_BADGE).setStyle(Style.EMPTY.withFont(Identifier.of("mcc", "icon")));
    }
}
