package me.anastarawneh.mccinametagmod.nametags;

import me.anastarawneh.mccinametagmod.MCCINametagMod;
import me.anastarawneh.mccinametagmod.util.UnicodeChars;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class ParkourWarriorDojo {
    private static int MEDALS = -1;

    public static void setNametag() {
        String playerName = MinecraftClient.getInstance().player.getName().getString();
        
        Nametags.TOP_LABEL = Text.literal(playerName).setStyle(Style.EMPTY.withColor(MCCINametagMod.COLOR));
        int crownLevel = MinecraftClient.getInstance().player.experienceLevel;
        for (PlayerListEntry entry : MinecraftClient.getInstance().player.networkHandler.getListedPlayerListEntries())
            if (entry.getDisplayName() != null && entry.getDisplayName().toString().contains("color=yellow"))
                MEDALS = Integer.parseInt(entry.getDisplayName().getSiblings().get(1).getString());
        Nametags.BOTTOM_LABEL = Text.literal(MCCINametagMod.FACTION).setStyle(Style.EMPTY.withFont(new Identifier("mcc:icon")))
                .append(Text.literal(MCCINametagMod.FACTION_LEVEL + " ").setStyle(Style.EMPTY.withFont(new Identifier("minecraft:default"))))
                .append(Text.literal(MCCINametagMod.CROWN).setStyle(Style.EMPTY.withFont(new Identifier("mcc:icon"))))
                .append(Text.literal(crownLevel + " ").setStyle(Style.EMPTY.withColor(Formatting.YELLOW).withFont(new Identifier("minecraft:default"))))
                .append(Text.literal(UnicodeChars.MedalUnicode).setStyle(Style.EMPTY.withFont(new Identifier("mcc:icon"))))
                .append(Text.literal(String.valueOf(MEDALS)).setStyle(Style.EMPTY.withColor(Formatting.YELLOW).withFont(new Identifier("minecraft:default"))));
        Nametags.RANK_LABEL = Text.empty();
    }
}
