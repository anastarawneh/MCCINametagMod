package me.anastarawneh.mccinametagmod.mixin;

import me.anastarawneh.mccinametagmod.MCCINametagMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity> extends EntityRendererMixin<T> {
    @Inject(method= "hasLabel(Lnet/minecraft/entity/LivingEntity;)Z", at=@At("TAIL"), cancellable = true)
    private void overrideHasLabel(LivingEntity livingEntity, CallbackInfoReturnable<Boolean> cir) {
        if (!MCCINametagMod.modEnabled()) return;

        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        ClientPlayerEntity clientPlayerEntity = minecraftClient.player;
        boolean bl = !livingEntity.isInvisibleTo(clientPlayerEntity);

        boolean modifiedCheck = MinecraftClient.isHudEnabled() && bl && !livingEntity.hasPassengers();
        boolean currentScreen = minecraftClient.currentScreen == null || minecraftClient.currentScreen instanceof ChatScreen;
        cir.setReturnValue(modifiedCheck && currentScreen);
    }

    @Override
    public void renderLabelIfPresent(T entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (!(entity instanceof PlayerEntity player) || !player.isMainPlayer()) {
            super.renderLabelIfPresent(entity, text, matrices, vertexConsumers, light, ci);
            return;
        }

        if (!MCCINametagMod.modEnabled()) {
            ci.cancel();
            return;
        }

        ci.cancel();
        MutableText topLabel;
        MutableText bottomLabel;

        String playerName = player.getGameProfile().getName();
        try {
            String header = ((PlayerListHudAccessor) MinecraftClient.getInstance().inGameHud.getPlayerListHud()).getHeader().getString();
            if (header.contains("HOLE IN THE WALL") || header.contains("TGTTOS")) {
                topLabel = Text.literal("");
                bottomLabel = Text.literal(MCCINametagMod.RANK + MCCINametagMod.TEAM + " ").setStyle(Style.EMPTY.withColor(MCCINametagMod.COLOR).withFont(new Identifier("mcc:icon"))).append(Text.literal(playerName).setStyle(Style.EMPTY.withFont(new Identifier("minecraft:default"))));
            }
            else if (header.contains("BATTLE BOX")) {
                Text text1 = MinecraftClient.getInstance().getNetworkHandler().getPlayerList().stream().map(PlayerListEntry::getDisplayName).filter(t -> t != null && t.getString().contains(MinecraftClient.getInstance().player.getGameProfile().getName())).findFirst().get();
                Style style = text1.getSiblings().get(0).getSiblings().get(0).getStyle();

                if (MCCINametagMod.GAME_STAGE == 0) {
                    topLabel = Text.literal("");
                    bottomLabel = Text.literal(playerName).setStyle(Style.EMPTY.withColor(style.getColor()));
                    MCCINametagMod.TEAM_COLOR = style.getColor();
                }
                else if (MCCINametagMod.GAME_STAGE == 1) {
                    topLabel = Text.literal("\uE0F7").setStyle(Style.EMPTY.withColor(style.getColor()).withFont(new Identifier("mcc:icon")))
                            .append(Text.literal(" ").setStyle(Style.EMPTY.withColor(Formatting.WHITE)));

                    float health = player.getHealth();

                    if (health >= 4) topLabel = topLabel.append(Text.literal("\uE087").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));
                    else if (health == 3) topLabel = topLabel.append(Text.literal("\uE086").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));
                    else if (health == 2) topLabel = topLabel.append(Text.literal("\uE085").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));
                    else topLabel = topLabel.append(Text.literal("\uE084").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));

                    // I think on the server these paddings are positive but this looks better
                    topLabel = topLabel.append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                    if (health >= 8) topLabel = topLabel.append(Text.literal("\uE087").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));
                    else if (health == 7) topLabel = topLabel.append(Text.literal("\uE086").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));
                    else if (health == 6) topLabel = topLabel.append(Text.literal("\uE085").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));
                    else if (health == 5) topLabel = topLabel.append(Text.literal("\uE084").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));
                    else topLabel = topLabel.append(Text.literal("\uE083").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));

                    topLabel = topLabel.append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                    if (health >= 12) topLabel = topLabel.append(Text.literal("\uE087").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));
                    else if (health == 11) topLabel = topLabel.append(Text.literal("\uE086").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));
                    else if (health == 10) topLabel = topLabel.append(Text.literal("\uE085").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));
                    else if (health == 9) topLabel = topLabel.append(Text.literal("\uE084").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));
                    else topLabel = topLabel.append(Text.literal("\uE083").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));

                    topLabel = topLabel.append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                    if (health >= 16) topLabel = topLabel.append(Text.literal("\uE087").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));
                    else if (health == 15) topLabel = topLabel.append(Text.literal("\uE086").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));
                    else if (health == 14) topLabel = topLabel.append(Text.literal("\uE085").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));
                    else if (health == 13) topLabel = topLabel.append(Text.literal("\uE084").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));
                    else topLabel = topLabel.append(Text.literal("\uE083").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));

                    topLabel = topLabel.append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                    if (health >= 20) topLabel = topLabel.append(Text.literal("\uE087").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));
                    else if (health == 19) topLabel = topLabel.append(Text.literal("\uE086").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));
                    else if (health == 18) topLabel = topLabel.append(Text.literal("\uE085").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));
                    else if (health == 17) topLabel = topLabel.append(Text.literal("\uE084").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));
                    else topLabel = topLabel.append(Text.literal(switch ((int) health) {
                            case 16 -> "\uE097";
                            case 15 -> "\uE096";
                            case 14 -> "\uE095";
                            case 13 -> "\uE094";
                            case 12 -> "\uE093";
                            case 11 -> "\uE092";
                            case 10 -> "\uE091";
                            case  9 -> "\uE090";
                            case  8 -> "\uE08F";
                            case  7 -> "\uE08E";
                            case  6 -> "\uE08D";
                            case  5 -> "\uE08C";
                            case  4 -> "\uE08B";
                            case  3 -> "\uE08A";
                            case  2 -> "\uE089";
                            case  1 -> "\uE088";
                            default -> "\uE083";
                        }).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));

                    bottomLabel = Text.literal(playerName).setStyle(Style.EMPTY.withColor(style.getColor()).withFont(new Identifier("minecraft:default")));
                }
                else {
                    topLabel = Text.literal("");
                    bottomLabel = Text.literal(MCCINametagMod.RANK + MCCINametagMod.TEAM + " ").setStyle(Style.EMPTY.withColor(MCCINametagMod.COLOR).withFont(new Identifier("mcc:icon"))).append(Text.literal(playerName).setStyle(Style.EMPTY.withFont(new Identifier("minecraft:default"))));
                }
            }
            else if (header.contains("SKY BATTLE")) {
                Text text1 = MinecraftClient.getInstance().getNetworkHandler().getPlayerList().stream().map(PlayerListEntry::getDisplayName).filter(t -> t != null && t.getString().contains(MinecraftClient.getInstance().player.getGameProfile().getName())).findFirst().get();
                Style style = text1.getSiblings().get(0).getStyle();

                if (MCCINametagMod.GAME_STAGE == 0) {
                    topLabel = Text.literal("");
                    bottomLabel = Text.literal(playerName).setStyle(Style.EMPTY.withColor(style.getColor()));
                    MCCINametagMod.TEAM_COLOR = style.getColor();
                }
                else if (MCCINametagMod.GAME_STAGE == 1) {
                    topLabel = Text.literal("\uE0F7").setStyle(Style.EMPTY.withColor(style.getColor()).withFont(new Identifier("mcc:icon")))
                            .append(Text.literal(" ").setStyle(Style.EMPTY.withColor(Formatting.WHITE)));

                    float health = player.getHealth();

                    if (health >= 4) topLabel = topLabel.append(Text.literal("\uE087").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));
                    else if (health == 3) topLabel = topLabel.append(Text.literal("\uE086").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));
                    else if (health == 2) topLabel = topLabel.append(Text.literal("\uE085").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));
                    else topLabel = topLabel.append(Text.literal("\uE084").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));

                    // I think on the server these paddings are positive but this looks better
                    topLabel = topLabel.append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                    if (health >= 8) topLabel = topLabel.append(Text.literal("\uE087").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));
                    else if (health == 7) topLabel = topLabel.append(Text.literal("\uE086").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));
                    else if (health == 6) topLabel = topLabel.append(Text.literal("\uE085").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));
                    else if (health == 5) topLabel = topLabel.append(Text.literal("\uE084").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));
                    else topLabel = topLabel.append(Text.literal("\uE083").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));

                    topLabel = topLabel.append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                    if (health >= 12) topLabel = topLabel.append(Text.literal("\uE087").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));
                    else if (health == 11) topLabel = topLabel.append(Text.literal("\uE086").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));
                    else if (health == 10) topLabel = topLabel.append(Text.literal("\uE085").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));
                    else if (health == 9) topLabel = topLabel.append(Text.literal("\uE084").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));
                    else topLabel = topLabel.append(Text.literal("\uE083").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));

                    topLabel = topLabel.append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                    if (health >= 16) topLabel = topLabel.append(Text.literal("\uE087").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));
                    else if (health == 15) topLabel = topLabel.append(Text.literal("\uE086").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));
                    else if (health == 14) topLabel = topLabel.append(Text.literal("\uE085").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));
                    else if (health == 13) topLabel = topLabel.append(Text.literal("\uE084").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));
                    else topLabel = topLabel.append(Text.literal("\uE083").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));

                    topLabel = topLabel.append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                    if (health >= 20) topLabel = topLabel.append(Text.literal("\uE087").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));
                    else if (health == 19) topLabel = topLabel.append(Text.literal("\uE086").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));
                    else if (health == 18) topLabel = topLabel.append(Text.literal("\uE085").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));
                    else if (health == 17) topLabel = topLabel.append(Text.literal("\uE084").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));
                    else topLabel = topLabel.append(Text.literal(switch ((int) health) {
                            case 16 -> "\uE097";
                            case 15 -> "\uE096";
                            case 14 -> "\uE095";
                            case 13 -> "\uE094";
                            case 12 -> "\uE093";
                            case 11 -> "\uE092";
                            case 10 -> "\uE091";
                            case  9 -> "\uE090";
                            case  8 -> "\uE08F";
                            case  7 -> "\uE08E";
                            case  6 -> "\uE08D";
                            case  5 -> "\uE08C";
                            case  4 -> "\uE08B";
                            case  3 -> "\uE08A";
                            case  2 -> "\uE089";
                            case  1 -> "\uE088";
                            default -> "\uE083";
                        }).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mcc:icon"))));

                    bottomLabel = Text.literal(playerName).setStyle(Style.EMPTY.withColor(style.getColor()).withFont(new Identifier("minecraft:default")));
                }
                else {
                    topLabel = Text.literal("");
                    bottomLabel = Text.literal(MCCINametagMod.RANK + MCCINametagMod.TEAM + " ").setStyle(Style.EMPTY.withColor(MCCINametagMod.COLOR).withFont(new Identifier("mcc:icon"))).append(Text.literal(playerName).setStyle(Style.EMPTY.withFont(new Identifier("minecraft:default"))));
                }
            }
            else {
                topLabel = Text.literal(playerName).setStyle(Style.EMPTY.withColor(MCCINametagMod.COLOR));
                Text actionBar = ((IngameHudAccessor) MinecraftClient.getInstance().inGameHud).getOverlayMessage();
                int factionLevel = player.experienceLevel;
                int achievementpoints = Integer.parseInt(actionBar.getSiblings().get(5).getSiblings().get(1).getSiblings().get(1).getSiblings().get(1).getString());
                bottomLabel = Text.literal(MCCINametagMod.TEAM).setStyle(Style.EMPTY.withFont(new Identifier("mcc:icon")))
                        .append(Text.literal(factionLevel + " ").setStyle(Style.EMPTY.withFont(new Identifier("minecraft:default"))))
                        .append(Text.literal("\uE00B").setStyle(Style.EMPTY.withFont(new Identifier("mcc:icon"))))
                        .append(Text.literal(String.valueOf(achievementpoints)).setStyle(Style.EMPTY.withColor(Formatting.YELLOW).withFont(new Identifier("minecraft:default"))));
            }
        } catch (Exception ex) {
            topLabel = Text.literal(playerName).setStyle(Style.EMPTY.withColor(Formatting.GRAY));
            bottomLabel = Text.literal("Error.").setStyle(Style.EMPTY.withColor(Formatting.DARK_RED));
        }

        double d = this.dispatcher.getSquaredDistanceToCamera(entity);
        if (!(d > 4096.0)) {
            boolean bl = !entity.isSneaky();
            float f = entity.getHeight() + 0.55F;
            int i = "deadmau5".equals(bottomLabel.getString()) ? -10 : 0;
            matrices.push();
            matrices.translate(0.0, (double)f, 0.0);
            matrices.multiply(this.dispatcher.getRotation());
            matrices.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = matrices.peek().getPositionMatrix();
            float g = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F);
            int j = (int)(g * 255.0F) << 24;
            TextRenderer textRenderer = this.getTextRenderer();
            float h = (float)(-textRenderer.getWidth(bottomLabel) / 2);
            textRenderer.draw(bottomLabel, h, (float)i, 553648127, false, matrix4f, vertexConsumers, bl, j, light);
            if (bl) {
                textRenderer.draw(bottomLabel, h, (float)i, -1, false, matrix4f, vertexConsumers, false, 0, light);
            }

            matrices.pop();

            if (!topLabel.getString().equals("")) {
                f = entity.getHeight() + 0.92F;
                i = "deadmau5".equals(topLabel.getString()) ? -10 : 0;
                matrices.push();
                matrices.translate(0.0, (double) f, 0.0);
                matrices.multiply(getDispatcher().getRotation());
                matrices.scale(-0.025F, -0.025F, 0.025F);
                matrix4f = matrices.peek().getPositionMatrix();
                h = (float)(-textRenderer.getWidth(topLabel) / 2);
                textRenderer.draw(topLabel, h, (float) i, 553648127, false, matrix4f, vertexConsumers, bl, j, light);
                if (bl) {
                    textRenderer.draw(topLabel, h, (float) i, -1, false, matrix4f, vertexConsumers, false, 0, light);
                }

                matrices.pop();
            }
        }
    }
}
