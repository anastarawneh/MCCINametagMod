package me.anastarawneh.mccinametagmod.nametags;

import me.anastarawneh.mccinametagmod.MCCINametagMod;
import me.anastarawneh.mccinametagmod.util.UnicodeChars;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class Dynaball {
    private static TextColor TEAM_COLOR = TextColor.fromFormatting(Formatting.GRAY);

    public static void setNametag() {
        String playerName = MinecraftClient.getInstance().player.getName().getString();

        if (!MCCINametagMod.STAGE.equals("podiumPhase")) {
            if (MCCINametagMod.STAGE.isEmpty()) {
                Text text = MinecraftClient.getInstance().getNetworkHandler().getPlayerList().stream().map(PlayerListEntry::getDisplayName).filter(t -> t != null && t.getString().contains(MinecraftClient.getInstance().player.getGameProfile().getName())).findFirst().get();
                Style style = text.getSiblings().getFirst().getSiblings().getFirst().getStyle();
                TEAM_COLOR = style.getColor();
            }

            Nametags.TOP_LABEL = Text.empty();
            Nametags.BOTTOM_LABEL = Text.literal(UnicodeChars.TeamFlag).setStyle(Style.EMPTY.withColor(TEAM_COLOR).withFont(Identifier.of("mcc", "icon")))
                    .append(Text.literal(MCCINametagMod.RANK + MCCINametagMod.CROWN).setStyle(Style.EMPTY.withColor(MCCINametagMod.COLOR).withFont(Identifier.of("mcc", "icon"))))
                    .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(Identifier.of("mcc", "padding_nosplit"))))
                    .append(Text.literal(playerName).setStyle(Style.EMPTY.withFont(Identifier.of("minecraft", "default")).withColor(TEAM_COLOR)));
            Nametags.RANK_LABEL = Text.empty();
        }
        else {
            Nametags.TOP_LABEL = Text.empty();
            Nametags.BOTTOM_LABEL = Nametags.getStandardBottomText(true);
            Nametags.RANK_LABEL = Text.empty();
        }
    }
}
