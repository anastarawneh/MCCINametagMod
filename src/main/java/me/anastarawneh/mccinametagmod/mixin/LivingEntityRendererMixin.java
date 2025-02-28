package me.anastarawneh.mccinametagmod.mixin;

import me.anastarawneh.mccinametagmod.MCCINametagMod;
import me.anastarawneh.mccinametagmod.nametags.*;
import me.anastarawneh.mccinametagmod.util.Game;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityAttachmentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity> extends EntityRendererMixin<T> {
    @Unique
    private boolean applyLabel(LivingEntity livingEntity) {
        if (!MCCINametagMod.modEnabled()) return false;
        if (!(livingEntity instanceof PlayerEntity player) || !player.isMainPlayer()) return false;

        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        ClientPlayerEntity clientPlayerEntity = minecraftClient.player;
        boolean bl = !livingEntity.isInvisibleTo(clientPlayerEntity);

        boolean modifiedCheck = MinecraftClient.isHudEnabled() && bl && !livingEntity.hasPassengers();
        boolean currentScreen = minecraftClient.currentScreen == null || minecraftClient.currentScreen instanceof ChatScreen || (minecraftClient.currentScreen instanceof InventoryScreen && MCCINametagMod.getConfig().showInInventory);
        return modifiedCheck && currentScreen;
    }

    @Override
    public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (applyLabel(entity)) renderLabel(entity, matrices, vertexConsumers, light, tickDelta);
    }

    @Unique
    public void renderLabel(T entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float tickDelta) {
        PlayerEntity player = (PlayerEntity) entity;
        String playerName = player.getGameProfile().getName();
        Game game = MCCINametagMod.GAME;
        try {
            if (game == Game.HOLE_IN_THE_WALL) {
                HoleInTheWall.setNametag();
            }
            else if (game == Game.TGTTOS) {
                TGTTOS.setNametag();
            }
            else if (game == Game.BATTLE_BOX) {
                float health = player.getHealth();
                BattleBox.setNametag(health);
            }
            else if (game == Game.SKY_BATTLE) {
                float health = player.getHealth();
                SkyBattle.setNametag(health);
            }
            else if (game == Game.PARKOUR_WARRIOR_DOJO) {
                ParkourWarriorDojo.setNametag();
            }
            else if (game == Game.PARKOUR_WARRIOR_SURVIVOR) {
                float health = player.getHealth();
                float maxHealth = player.getMaxHealth();
                ParkourWarriorSurvivor.setNametag(health, maxHealth);
            }
            else if (game == Game.DYNABALL) {
                Dynaball.setNametag();
            }
            else if (game == Game.ROCKET_SPLEEF_RUSH) {
                int maxDamage = player.getInventory().getArmorStack(2).getMaxDamage();
                int currentDamage = maxDamage - player.getInventory().getArmorStack(2).getDamage();
                int health = (int) Math.floor(((float) currentDamage) / maxDamage * 20);
                if (Objects.equals(player.getInventory().getArmorStack(2).getName().getString(), "Air")) health = 20;
                RocketSpleefRush.setNametag(health);
            }
            else {
                Lobby.setNametag();
            }
        } catch (Exception ex) {
            Nametags.TOP_LABEL = Text.literal(playerName).setStyle(Style.EMPTY.withColor(Formatting.GRAY));
            Nametags.BOTTOM_LABEL = Text.literal("Error.").setStyle(Style.EMPTY.withColor(Formatting.DARK_RED));
            Nametags.RANK_LABEL = Text.empty();
            MCCINametagMod.LATEST_EXCEPTION = ex;
        }

        double d = this.dispatcher.getSquaredDistanceToCamera(entity);
        if (!(d > 4096.0)) {
            Vec3d vec3d = entity.getAttachments().getPointNullable(EntityAttachmentType.NAME_TAG, 0, entity.getYaw(tickDelta));
            if (vec3d != null) {
                // TODO: check which of these actually matter
                double translateX = vec3d.x;
                double translateY = vec3d.y + 1.0;
                double translateZ = vec3d.z;

                boolean bl = !entity.isSneaky();
                int i = "deadmau5".equals(Nametags.BOTTOM_LABEL.getString()) ? -10 : 0;
                matrices.push();
                matrices.translate(translateX, translateY, translateZ);
                matrices.multiply(this.dispatcher.getRotation());
                matrices.scale(0.025F, -0.025F, 0.025F);
                Matrix4f matrix4f = matrices.peek().getPositionMatrix();
                float g = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F);
                int j = (int) (g * 255.0F) << 24;
                TextRenderer textRenderer = this.getTextRenderer();
                float h = (float) (-textRenderer.getWidth(Nametags.BOTTOM_LABEL) / 2);
                textRenderer.draw(Nametags.BOTTOM_LABEL, h, (float) i, 553648127, false, matrix4f, vertexConsumers, bl ? TextRenderer.TextLayerType.SEE_THROUGH : TextRenderer.TextLayerType.NORMAL, j, light);
                if (bl) {
                    textRenderer.draw(Nametags.BOTTOM_LABEL, h, (float) i, -1, false, matrix4f, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, light);
                }

                matrices.pop();

                if (!Nametags.TOP_LABEL.getString().isEmpty()) {
                    i = "deadmau5".equals(Nametags.TOP_LABEL.getString()) ? -10 : 0;
                    matrices.push();
                    matrices.translate(translateX, translateY + 0.5, translateZ);
                    matrices.multiply(getDispatcher().getRotation());
                    matrices.scale(0.025F, -0.025F, 0.025F);
                    matrix4f = matrices.peek().getPositionMatrix();
                    h = (float) (-textRenderer.getWidth(Nametags.TOP_LABEL) / 2);
                    textRenderer.draw(Nametags.TOP_LABEL, h, (float) i, 553648127, false, matrix4f, vertexConsumers, bl ? TextRenderer.TextLayerType.SEE_THROUGH : TextRenderer.TextLayerType.NORMAL, j, light);
                    if (bl) {
                        textRenderer.draw(Nametags.TOP_LABEL, h, (float) i, -1, false, matrix4f, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, light);
                    }

                    matrices.pop();
                }

                if (!Nametags.RANK_LABEL.getString().isEmpty()) {
                    i = "deadmau5".equals(Nametags.RANK_LABEL.getString()) ? -10 : 0;
                    matrices.push();
                    matrices.translate(translateX, translateY + 1.0, translateZ);
                    matrices.multiply(getDispatcher().getRotation());
                    matrices.scale(0.025F, -0.025F, 0.025F);
                    matrix4f = matrices.peek().getPositionMatrix();
                    h = (float) (-textRenderer.getWidth(Nametags.RANK_LABEL) / 2);
                    textRenderer.draw(Nametags.RANK_LABEL, h, (float) i, 553648127, false, matrix4f, vertexConsumers, bl ? TextRenderer.TextLayerType.SEE_THROUGH : TextRenderer.TextLayerType.NORMAL, 0, light);
                    if (bl) {
                        textRenderer.draw(Nametags.RANK_LABEL, h, (float) i, -1, false, matrix4f, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, light);
                    }

                    matrices.pop();
                }
            }
        }
    }
}
