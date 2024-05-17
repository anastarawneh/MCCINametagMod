package me.anastarawneh.mccinametagmod.mixin;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {
    @Shadow @Final protected EntityRenderDispatcher dispatcher;
    @Shadow @Final private TextRenderer textRenderer;

    @Unique
    public EntityRenderDispatcher getDispatcher() {
        return dispatcher;
    }
    @Unique
    public TextRenderer getTextRenderer() {
        return textRenderer;
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {

    }
}
