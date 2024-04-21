package me.anastarawneh.mccinametagmod.mixin;

import me.anastarawneh.mccinametagmod.MCCINametagMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.server.ServerResourcePackLoader;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerResourcePackLoader.class)
public class ServerResourcePackLoaderMixin {
    @Unique public boolean sentFactionLevelWarning = false;

    @Inject(method = "onReloadSuccess", at = @At("TAIL"))
    public void onReloadSuccess(CallbackInfo ci) {
        if (MCCINametagMod.modEnabled()) {
            if (MCCINametagMod.FACTION_LEVEL == -1 && !sentFactionLevelWarning) {
                MinecraftClient.getInstance().player.sendMessage(MCCINametagMod.MESSAGE_PREFIX.copy().append(Text.literal("Due to an MCC Island update, your faction level could not be obtained. Please open your wardrobe to fix.").setStyle(Style.EMPTY.withColor(Formatting.RED))));
                sentFactionLevelWarning = true;
            }
        }
    }
}
