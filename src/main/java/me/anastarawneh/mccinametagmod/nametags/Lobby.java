package me.anastarawneh.mccinametagmod.nametags;

import me.anastarawneh.mccinametagmod.MCCINametagMod;
import me.anastarawneh.mccinametagmod.util.Game;
import me.anastarawneh.mccinametagmod.util.UnicodeChars;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.scoreboard.*;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class Lobby {
    public static void setNametag() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        String playerName = player.getName().getString();

        Nametags.TOP_LABEL = Text.literal(playerName).setStyle(Style.EMPTY.withColor(MCCINametagMod.COLOR));
        int crownLevel = player.experienceLevel;
        Nametags.BOTTOM_LABEL = Text.literal(MCCINametagMod.FACTION).setStyle(Style.EMPTY.withFont(new Identifier("mcc:icon")))
                .append(Text.literal(MCCINametagMod.FACTION_LEVEL + " ").setStyle(Style.EMPTY.withFont(new Identifier("minecraft:default"))))
                .append(Text.literal(MCCINametagMod.CROWN).setStyle(Style.EMPTY.withFont(new Identifier("mcc:icon"))))
                .append(Text.literal(String.valueOf(crownLevel)).setStyle(Style.EMPTY.withColor(Formatting.YELLOW).withFont(new Identifier("minecraft:default"))));
        Nametags.RANK_LABEL = Text.literal(MCCINametagMod.RANK_BADGE).setStyle(Style.EMPTY.withFont(new Identifier("mcc:icon")));

        if (MCCINametagMod.GAME == Game.PARKOUR_WARRIOR_LOBBY)
        {
            Scoreboard scoreboard = MinecraftClient.getInstance().player.getScoreboard();
            ScoreboardObjective obj = scoreboard.getObjectiveForSlot(ScoreboardDisplaySlot.SIDEBAR);
            int medals = -1;
            for (ScoreboardEntry entry : scoreboard.getScoreboardEntries(obj).stream().toList()) {
                Team team = scoreboard.getScoreHolderTeam(entry.owner());
                if (!team.getPrefix().getString().contains("Total Unique Medals")) continue;
                medals = Integer.parseInt(team.getPrefix()
                        .getSiblings().getFirst().getSiblings().getFirst().getSiblings().getFirst().getSiblings().getFirst().getString().replace(",", ""));
            }
            Nametags.BOTTOM_LABEL = Nametags.BOTTOM_LABEL
                    .append(Text.literal(" " + UnicodeChars.MedalUnicode).setStyle(Style.EMPTY.withFont(new Identifier("mcc:icon"))))
                    .append(Text.literal(String.valueOf(medals)).setStyle(Style.EMPTY.withColor(Formatting.YELLOW).withFont(new Identifier("minecraft:default"))));
        }
    }
}
