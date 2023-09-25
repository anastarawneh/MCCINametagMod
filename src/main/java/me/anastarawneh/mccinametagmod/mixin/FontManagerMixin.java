package me.anastarawneh.mccinametagmod.mixin;

import net.minecraft.client.font.Font;
import net.minecraft.client.font.FontLoader;
import net.minecraft.client.font.FontManager;
import net.minecraft.resource.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(FontManager.class)
public class FontManagerMixin {
    @Inject(method = "load(Lnet/minecraft/client/font/FontManager$FontKey;Lnet/minecraft/client/font/FontLoader$Loadable;Lnet/minecraft/resource/ResourceManager;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;", at = @At("HEAD"), cancellable = true)
    public void load(FontManager.FontKey key, FontLoader.Loadable loadable, ResourceManager resourceManager, Executor executor, CallbackInfoReturnable<CompletableFuture<Optional<Font>>> cir) {
        if (key.fontId().getNamespace().equals("mccinametagmod")) {
            cir.setReturnValue(CompletableFuture.supplyAsync(() -> {
                try {
                    return Optional.of(loadable.load(resourceManager));
                } catch (Exception var4) {
                    return Optional.empty();
                }
            }, executor));
        }
    }
}
