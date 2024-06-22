package me.anastarawneh.mccinametagmod.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.anastarawneh.mccinametagmod.MCCINametagMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.util.Objects;

public class Util {
    public static TextColor GetFactionColor() {
        try {
            Resource resource = MinecraftClient.getInstance().getResourceManager().getAllResources(Identifier.of("mcc", "font/icon.json")).get(0);
            String json = resource.getReader().readLine();
            String iconPath = "";

            JsonObject icons = JsonHelper.deserialize(json);
            JsonArray providers = icons.get("providers").getAsJsonArray();
            for (JsonElement icon : providers) {
                if (!icon.getAsJsonObject().get("type").getAsString().equals("bitmap")) continue;
                if (Objects.equals(icon.getAsJsonObject().get("chars").getAsJsonArray().get(0).getAsString(), MCCINametagMod.FACTION)) {
                    iconPath = icon.getAsJsonObject().get("file").getAsString();
                    break;
                }
            }

            String teamColor = iconPath.split("/")[3];
            return switch (teamColor) {
                case "red" -> TextColor.fromRgb(16733524);
                case "orange" -> TextColor.fromFormatting(Formatting.GOLD);
                case "yellow" -> TextColor.fromFormatting(Formatting.YELLOW);
                case "lime" -> TextColor.fromFormatting(Formatting.GREEN);
                case "green" -> TextColor.fromFormatting(Formatting.DARK_GREEN);
                case "cyan" -> TextColor.fromRgb(48025);
                case "aqua" -> TextColor.fromRgb(5627391);
                case "blue" -> TextColor.fromRgb(5605631);
                case "purple" -> TextColor.fromRgb(8926207);
                case "pink" -> TextColor.fromFormatting(Formatting.LIGHT_PURPLE);
                default -> TextColor.fromFormatting(Formatting.GRAY);
            };
        } catch (Exception ex) {
            return TextColor.fromFormatting(Formatting.GRAY);
        }
    }
}
