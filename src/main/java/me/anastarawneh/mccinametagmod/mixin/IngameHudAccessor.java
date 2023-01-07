package me.anastarawneh.mccinametagmod.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(InGameHud.class)
public interface IngameHudAccessor {
    @Accessor
    Text getOverlayMessage();
}
