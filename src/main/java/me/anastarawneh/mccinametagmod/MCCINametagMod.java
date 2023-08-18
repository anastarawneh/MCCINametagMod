package me.anastarawneh.mccinametagmod;

import me.anastarawneh.mccinametagmod.config.Config;
import me.anastarawneh.mccinametagmod.util.Game;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MCCINametagMod implements ModInitializer {
    public static final String MODID = "mccinametagmod";
    public static final Logger LOGGER = LogManager.getLogger();
    public static String TEAM = "";
    public static String RANK = "";
    public static TextColor COLOR = TextColor.fromFormatting(Formatting.DARK_GRAY);
    public static TextColor FACTION_COLOR = TextColor.fromFormatting(Formatting.GRAY);
    public static Game GAME = Game.NONE;
    public static String STAGE = "";
    public static String PHASE_TYPE = "";

    @Override
    public void onInitialize() {
        AutoConfig.register(Config.class, Toml4jConfigSerializer::new);
        LOGGER.info("Hello there");
    }

    public static Config getConfig() {
        return AutoConfig.getConfigHolder(Config.class).getConfig();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean modEnabled() {
        Config config = getConfig();
        return config.enabled &&
                !MinecraftClient.getInstance().isInSingleplayer() &&
                MinecraftClient.getInstance().getCurrentServerEntry() != null &&
                MinecraftClient.getInstance().getCurrentServerEntry().address.endsWith("mccisland.net");
    }
}
