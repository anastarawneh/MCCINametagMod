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

public class BattleBox {
    private static TextColor TEAM_COLOR = TextColor.fromFormatting(Formatting.GRAY);
    private static int HEALTH = 20;

    public static void setNametag(float health) {
        HEALTH = (int) health;
        setNametag();
    }
    
    public static void setNametag() {
        String playerName = MinecraftClient.getInstance().player.getName().getString();

        if (MCCINametagMod.STAGE.isEmpty()) {
            Text text = MinecraftClient.getInstance().getNetworkHandler().getPlayerList().stream().map(PlayerListEntry::getDisplayName).filter(t -> t != null && t.getString().contains(playerName)).findFirst().get();
            Style style = text.getSiblings().getFirst().getSiblings().getFirst().getSiblings().get(4).getStyle();
            TEAM_COLOR = style.getColor();

            Nametags.TOP_LABEL = Text.empty();
            Nametags.BOTTOM_LABEL = Text.literal(playerName).setStyle(Style.EMPTY.withColor(TEAM_COLOR));
            Nametags.RANK_LABEL = Text.empty();
        }
        else if (!MCCINametagMod.STAGE.equals("podiumPhase") && !MCCINametagMod.STAGE.equals("postGame")) {
            Nametags.TOP_LABEL = Text.literal(UnicodeChars.TeamFlagBig).setStyle(Style.EMPTY.withColor(TEAM_COLOR).withFont(new Identifier("mcc:icon")))
                    .append(Text.literal(" ").setStyle(Style.EMPTY.withColor(Formatting.WHITE)))
                    .append(Nametags.getHealthBar(HEALTH));
            Nametags.BOTTOM_LABEL = Text.literal(playerName).setStyle(Style.EMPTY.withColor(TEAM_COLOR).withFont(new Identifier("minecraft:default")));
            Nametags.RANK_LABEL = Text.empty();
        }
        else if (MCCINametagMod.STAGE.equals("postGame")) {
            Nametags.TOP_LABEL = Text.empty();
            Nametags.BOTTOM_LABEL = Text.literal(playerName).setStyle(Style.EMPTY.withColor(TEAM_COLOR));
            Nametags.RANK_LABEL = Text.empty();
        }
        else {
            Nametags.TOP_LABEL = Text.empty();
            Nametags.BOTTOM_LABEL = Nametags.getStandardBottomText(true);
            Nametags.RANK_LABEL = Text.empty();
        }
    }
}
