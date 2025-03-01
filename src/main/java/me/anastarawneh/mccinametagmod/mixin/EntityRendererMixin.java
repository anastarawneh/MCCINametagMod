package me.anastarawneh.mccinametagmod.mixin;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
//? if >=1.21.2
/*import net.minecraft.client.render.entity.state.EntityRenderState;*/
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
//? if >=1.21.2 {
/*public class EntityRendererMixin<T extends Entity, S extends EntityRenderState> {
*///?} else {
public class EntityRendererMixin<T extends Entity> {
//?}
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

    //? if >=1.21.2 {
    /*@Inject(method = "updateRenderState", at = @At("HEAD"))
    public void updateRenderState(Entity entity, EntityRenderState state, float tickDelta, CallbackInfo ci) {}

    @Inject(method = "render", at = @At("HEAD"))
    public void render(EntityRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {}
    *///?} else {
    @Inject(method = "render", at = @At("HEAD"))
    public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {}
    //?}
}
