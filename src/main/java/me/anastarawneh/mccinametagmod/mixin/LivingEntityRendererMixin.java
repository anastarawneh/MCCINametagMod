package me.anastarawneh.mccinametagmod.mixin;

import me.anastarawneh.mccinametagmod.MCCINametagMod;
import me.anastarawneh.mccinametagmod.util.Game;
import me.anastarawneh.mccinametagmod.util.UnicodeChars;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.*;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity> extends EntityRendererMixin<T> {
    @Inject(method= "hasLabel(Lnet/minecraft/entity/LivingEntity;)Z", at=@At("TAIL"), cancellable = true)
    private void overrideHasLabel(LivingEntity livingEntity, CallbackInfoReturnable<Boolean> cir) {
        if (!MCCINametagMod.modEnabled()) return;

        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        ClientPlayerEntity clientPlayerEntity = minecraftClient.player;
        boolean bl = !livingEntity.isInvisibleTo(clientPlayerEntity);

        boolean modifiedCheck = MinecraftClient.isHudEnabled() && bl && !livingEntity.hasPassengers();
        boolean currentScreen = minecraftClient.currentScreen == null || minecraftClient.currentScreen instanceof ChatScreen || (minecraftClient.currentScreen instanceof InventoryScreen && MCCINametagMod.getConfig().showInInventory);
        cir.setReturnValue(modifiedCheck && currentScreen);
    }

    @Unique
    private String playerName = "";

    @Unique
    private MutableText getStandardBottomText(boolean faction) {
        if (faction) return Text.literal(MCCINametagMod.RANK + MCCINametagMod.FACTION + MCCINametagMod.CROWN).setStyle(Style.EMPTY.withColor(MCCINametagMod.COLOR).withFont(new Identifier("mcc:icon")))
                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:padding_nosplit"))))
                .append(Text.literal(playerName).setStyle(Style.EMPTY.withFont(new Identifier("minecraft:default"))));
        else return Text.literal(MCCINametagMod.RANK + MCCINametagMod.CROWN).setStyle(Style.EMPTY.withColor(MCCINametagMod.COLOR).withFont(new Identifier("mcc:icon")))
                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:padding_nosplit"))))
                .append(Text.literal(playerName).setStyle(Style.EMPTY.withFont(new Identifier("minecraft:default"))));
    }

    @Override
    public void renderLabelIfPresent(T entity, Text oldtext, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
       if (!(entity instanceof PlayerEntity player) || !player.isMainPlayer()) {
            super.renderLabelIfPresent(entity, oldtext, matrices, vertexConsumers, light, ci);
            return;
        }

        if (!MCCINametagMod.modEnabled()) {
            ci.cancel();
            return;
        }

        ci.cancel();
        MutableText topLabel;
        MutableText bottomLabel;
        MutableText rankLabel;

        playerName = player.getGameProfile().getName();
        Game game = MCCINametagMod.GAME;
        String stage = MCCINametagMod.STAGE;
        String phaseType = MCCINametagMod.PHASE_TYPE;
        try {
            if (game == Game.HOLE_IN_THE_WALL || game == Game.TGTTOS) {
                if (!stage.equals("podiumPhase")) {
                    topLabel = Text.empty();
                    bottomLabel = getStandardBottomText(false);
                }
                else {
                    topLabel = Text.empty();
                    bottomLabel = getStandardBottomText(true);
                }
            }
            else if (game == Game.BATTLE_BOX) {
                if (stage.equals("")) {
                    Text text = MinecraftClient.getInstance().getNetworkHandler().getPlayerList().stream().map(PlayerListEntry::getDisplayName).filter(t -> t != null && t.getString().contains(playerName)).findFirst().get();
                    Style style = text.getSiblings().get(0).getSiblings().get(0).getSiblings().get(4).getStyle();
                    MCCINametagMod.TEAM_COLOR = style.getColor();
                    topLabel = Text.empty();
                    bottomLabel = Text.literal(playerName).setStyle(Style.EMPTY.withColor(MCCINametagMod.TEAM_COLOR));
                }
                else if (!stage.equals("podiumPhase") && !stage.equals("postGame")) {
                    topLabel = Text.literal(UnicodeChars.TeamFlagBig).setStyle(Style.EMPTY.withColor(MCCINametagMod.TEAM_COLOR).withFont(new Identifier("mccinametagmod:mcci_icons")))
                            .append(Text.literal(" ").setStyle(Style.EMPTY.withColor(Formatting.WHITE)));

                    float health = player.getHealth();

                    if (health >= 4) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 3) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_5).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 2) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar0_5).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));

                    // I think on the server these paddings are positive but this looks better
                    topLabel = topLabel.append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                    if (health >= 8) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 7) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_5).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 6) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 5) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar0_5).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar0_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));

                    topLabel = topLabel.append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                    if (health >= 12) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 11) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_5).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 10) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 9) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar0_5).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar0_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));

                    topLabel = topLabel.append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                    if (health >= 16) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 15) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_5).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 14) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 13) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar0_5).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar0_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));

                    topLabel = topLabel.append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                    if (health >= 20) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 19) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_5).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 18) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 17) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar0_5).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else topLabel = topLabel.append(Text.literal(switch ((int) health) {
                            case 16 -> UnicodeChars.HealthNum8_0;
                            case 15 -> UnicodeChars.HealthNum7_5;
                            case 14 -> UnicodeChars.HealthNum7_0;
                            case 13 -> UnicodeChars.HealthNum6_5;
                            case 12 -> UnicodeChars.HealthNum6_0;
                            case 11 -> UnicodeChars.HealthNum5_5;
                            case 10 -> UnicodeChars.HealthNum5_0;
                            case  9 -> UnicodeChars.HealthNum4_5;
                            case  8 -> UnicodeChars.HealthNum4_0;
                            case  7 -> UnicodeChars.HealthNum3_5;
                            case  6 -> UnicodeChars.HealthNum3_0;
                            case  5 -> UnicodeChars.HealthNum2_5;
                            case  4 -> UnicodeChars.HealthNum2_0;
                            case  3 -> UnicodeChars.HealthNum1_5;
                            case  2 -> UnicodeChars.HealthNum1_0;
                            case  1 -> UnicodeChars.HealthNum0_5;
                            default -> UnicodeChars.HealthBar0_0;
                        }).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));

                    bottomLabel = Text.literal(playerName).setStyle(Style.EMPTY.withColor(MCCINametagMod.TEAM_COLOR).withFont(new Identifier("minecraft:default")));
                }
                else if (stage.equals("postGame")) {
                    topLabel = Text.empty();
                    bottomLabel = Text.literal(playerName).setStyle(Style.EMPTY.withColor(MCCINametagMod.TEAM_COLOR));
                }
                else {
                    topLabel = Text.empty();
                    bottomLabel = getStandardBottomText(true);
                }
            }
            else if (game == Game.SKY_BATTLE) {
                if (stage.equals("") || stage.equals("preRound")) {
                    Text text = MinecraftClient.getInstance().getNetworkHandler().getPlayerList().stream().map(PlayerListEntry::getDisplayName).filter(t -> t != null && t.getString().contains(MinecraftClient.getInstance().getSession().getUsername())).findFirst().get();
                    Style style = text.getSiblings().get(0).getSiblings().get(4).getStyle();
                    MCCINametagMod.TEAM_COLOR = style.getColor();
                    topLabel = Text.empty();
                    bottomLabel = Text.literal(playerName).setStyle(Style.EMPTY.withColor(MCCINametagMod.TEAM_COLOR));
                }
                else if (stage.equals("inRound")) {
                    topLabel = Text.literal(UnicodeChars.TeamFlagBig).setStyle(Style.EMPTY.withColor(MCCINametagMod.TEAM_COLOR).withFont(new Identifier("mccinametagmod:mcci_icons")))
                            .append(Text.literal(" ").setStyle(Style.EMPTY.withColor(Formatting.WHITE)));

                    float health = player.getHealth();

                    if (health >= 4) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 3) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_5).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 2) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar0_5).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));

                    // I think on the server these paddings are positive but this looks better
                    topLabel = topLabel.append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                    if (health >= 8) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 7) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_5).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 6) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 5) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar0_5).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar0_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));

                    topLabel = topLabel.append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                    if (health >= 12) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 11) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_5).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 10) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 9) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar0_5).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar0_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));

                    topLabel = topLabel.append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                    if (health >= 16) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 15) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_5).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 14) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 13) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar0_5).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar0_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));

                    topLabel = topLabel.append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                    if (health >= 20) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 19) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_5).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 18) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 17) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar0_5).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else topLabel = topLabel.append(Text.literal(switch ((int) health) {
                            case 16 -> UnicodeChars.HealthNum8_0;
                            case 15 -> UnicodeChars.HealthNum7_5;
                            case 14 -> UnicodeChars.HealthNum7_0;
                            case 13 -> UnicodeChars.HealthNum6_5;
                            case 12 -> UnicodeChars.HealthNum6_0;
                            case 11 -> UnicodeChars.HealthNum5_5;
                            case 10 -> UnicodeChars.HealthNum5_0;
                            case  9 -> UnicodeChars.HealthNum4_5;
                            case  8 -> UnicodeChars.HealthNum4_0;
                            case  7 -> UnicodeChars.HealthNum3_5;
                            case  6 -> UnicodeChars.HealthNum3_0;
                            case  5 -> UnicodeChars.HealthNum2_5;
                            case  4 -> UnicodeChars.HealthNum2_0;
                            case  3 -> UnicodeChars.HealthNum1_5;
                            case  2 -> UnicodeChars.HealthNum1_0;
                            case  1 -> UnicodeChars.HealthNum0_5;
                            default -> UnicodeChars.HealthBar0_0;
                        }).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));

                    bottomLabel = Text.literal(playerName).setStyle(Style.EMPTY.withColor(MCCINametagMod.TEAM_COLOR).withFont(new Identifier("minecraft:default")));
                }
                else if (!stage.equals("podiumPhase")) {
                    topLabel = Text.empty();
                    bottomLabel = Text.literal(playerName).setStyle(Style.EMPTY.withColor(MCCINametagMod.TEAM_COLOR));
                }
                else {
                    topLabel = Text.empty();
                    bottomLabel = getStandardBottomText(true);
                }
            }
            else if (game == Game.PARKOUR_WARRIOR_DOJO) {
                topLabel = Text.literal(playerName).setStyle(Style.EMPTY.withColor(MCCINametagMod.COLOR));
                //Text actionBar = ((IngameHudAccessor) MinecraftClient.getInstance().inGameHud).getOverlayMessage();
                int crownLevel = player.experienceLevel;
                /*try {
                    achievementPoints = Integer.parseInt(actionBar.getSiblings().get(5).getSiblings().get(1).getSiblings().get(1).getSiblings().get(1).getString());
                }
                catch (Exception ignored) {

                }*/
                int medals = -1;
                for (PlayerListEntry entry : MinecraftClient.getInstance().player.networkHandler.getListedPlayerListEntries())
                    if (entry.getDisplayName() != null && entry.getDisplayName().toString().contains("color=yellow"))
                        medals = Integer.parseInt(entry.getDisplayName().getSiblings().get(1).getString());
                bottomLabel = Text.literal(MCCINametagMod.FACTION).setStyle(Style.EMPTY.withFont(new Identifier("mcc:icon")))
                        .append(Text.literal(MCCINametagMod.FACTION_LEVEL + " ").setStyle(Style.EMPTY.withFont(new Identifier("minecraft:default"))))
                        .append(Text.literal(MCCINametagMod.CROWN).setStyle(Style.EMPTY.withFont(new Identifier("mcc:icon"))))
                        .append(Text.literal(crownLevel + " ").setStyle(Style.EMPTY.withColor(Formatting.YELLOW).withFont(new Identifier("minecraft:default"))))
                        .append(Text.literal(UnicodeChars.MedalUnicode).setStyle(Style.EMPTY.withFont(new Identifier("mccinametagmod:mcci_icons"))))
                        .append(Text.literal(String.valueOf(medals)).setStyle(Style.EMPTY.withColor(Formatting.YELLOW).withFont(new Identifier("minecraft:default"))));
            }
            else if (game == Game.PARKOUR_WARRIOR_SURVIVOR) {
                if (!phaseType.equals("POST_GAME")) {
                    topLabel = Text.literal(UnicodeChars.TeamFlagBig).setStyle(Style.EMPTY.withColor(MCCINametagMod.FACTION_COLOR).withFont(new Identifier("mccinametagmod:mcci_icons")))
                            .append(Text.literal(" ").setStyle(Style.EMPTY.withColor(Formatting.WHITE)));

                    float health = player.getHealth() / 2;
                    float maxHealth = player.getMaxHealth() / 2;
                    // We can divide by two because hearts are only lost one at a time.

                    if (maxHealth == 1) {
                        topLabel = topLabel.append(Text.literal(UnicodeChars.DisabledHealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))))
                                .append(Text.literal(UnicodeChars.DisabledHealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))))
                                .append(Text.literal(UnicodeChars.DisabledHealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))))
                                .append(Text.literal(UnicodeChars.DisabledHealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))))
                                .append(Text.literal(UnicodeChars.DisabledHealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    }
                    else if (maxHealth == 3) {
                        if (health > 1) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));
                        else topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                        if (health == 3) topLabel = topLabel.append(Text.literal(UnicodeChars.DisabledHealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));
                        else topLabel = topLabel.append(Text.literal(UnicodeChars.DisabledHealthBar1_0Hit).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                        topLabel = topLabel.append(Text.literal(UnicodeChars.DisabledHealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))))
                                .append(Text.literal(UnicodeChars.DisabledHealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))))
                                .append(Text.literal(UnicodeChars.DisabledHealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    }
                    else if (maxHealth == 5) {
                        if (health > 1) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));
                        else topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                        if (health > 3) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));
                        else if (health == 3) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));
                        else topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar0_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                    .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                        if (health == 5) topLabel = topLabel.append(Text.literal(UnicodeChars.DisabledHealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));
                        else topLabel = topLabel.append(Text.literal(UnicodeChars.DisabledHealthBar1_0Hit).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                    .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                        topLabel = topLabel.append(Text.literal(UnicodeChars.DisabledHealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))))
                                .append(Text.literal(UnicodeChars.DisabledHealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    }
                    else if (maxHealth == 7) {
                        if (health > 1) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));
                        else topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                        if (health > 3) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));
                        else if (health == 3) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));
                        else topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar0_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                    .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                        if (health > 5) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));
                        else if (health == 5) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));
                        else topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar0_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                    .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                        if (health == 7) topLabel = topLabel.append(Text.literal(UnicodeChars.DisabledHealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));
                        else topLabel = topLabel.append(Text.literal(UnicodeChars.DisabledHealthBar1_0Hit).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                        topLabel = topLabel.append(Text.literal(UnicodeChars.DisabledHealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    }
                    else if (maxHealth == 8) {
                        if (health > 1) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));
                        else topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                        if (health > 3) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));
                        else if (health == 3) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));
                        else topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar0_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                    .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                        if (health > 5) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));
                        else if (health == 5) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));
                        else topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar0_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                    .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                        if (health > 7) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));
                        else if (health == 7) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));
                        else topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar0_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                    .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                        topLabel = topLabel.append(Text.literal(UnicodeChars.DisabledHealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    }
                    else if (maxHealth == 9) {
                        if (health > 1) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));
                        else topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                        if (health > 3) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));
                        else if (health == 3) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));
                        else topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar0_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                    .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                        if (health > 5) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));
                        else if (health == 5) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));
                        else topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar0_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                    .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                        if (health > 7) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));
                        else if (health == 7) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));
                        else topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar0_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                    .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                        if (health == 9) topLabel = topLabel.append(Text.literal(UnicodeChars.DisabledHealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));
                        else topLabel = topLabel.append(Text.literal(UnicodeChars.DisabledHealthBar1_0Hit).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));
                    }
                    else {
                        if (health > 1) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));
                        else topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                        if (health > 3) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));
                        else if (health == 3) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));
                        else topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar0_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                    .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                        if (health > 5) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));
                        else if (health == 5) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));
                        else topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar0_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                    .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                        if (health > 7) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));
                        else if (health == 7) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));
                        else topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar0_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                    .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                        if (health > 9) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));
                        else if (health == 9) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));
                        else topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar0_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))))
                                    .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));
                    }

                    bottomLabel = Text.literal(playerName).setStyle(Style.EMPTY.withColor(MCCINametagMod.FACTION_COLOR).withFont(new Identifier("minecraft:default")));
                }
                else if (stage.equals("postGame")) {
                    topLabel = Text.empty();
                    bottomLabel = Text.literal(playerName).setStyle(Style.EMPTY.withColor(MCCINametagMod.FACTION_COLOR));
                }
                else {
                    topLabel = Text.empty();
                    bottomLabel = getStandardBottomText(false);
                }
            }
            else if (game == Game.DYNABALL) {
                if (!stage.equals("podiumPhase")) {
                    if (stage.equals("")) {
                        Text text = MinecraftClient.getInstance().getNetworkHandler().getPlayerList().stream().map(PlayerListEntry::getDisplayName).filter(t -> t != null && t.getString().contains(MinecraftClient.getInstance().player.getGameProfile().getName())).findFirst().get();
                        Style style = text.getSiblings().get(0).getSiblings().get(0).getSiblings().get(4).getStyle();
                        MCCINametagMod.TEAM_COLOR = style.getColor();
                    }

                    topLabel = Text.empty();
                    bottomLabel = Text.literal(UnicodeChars.TeamFlag).setStyle(Style.EMPTY.withColor(MCCINametagMod.TEAM_COLOR).withFont(new Identifier("mccinametagmod:mcci_icons")))
                            .append(Text.literal(MCCINametagMod.RANK + MCCINametagMod.CROWN).setStyle(Style.EMPTY.withColor(MCCINametagMod.COLOR).withFont(new Identifier("mcc:icon")))
                                    .append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:padding_nosplit"))))
                                    .append(Text.literal(playerName).setStyle(Style.EMPTY.withFont(new Identifier("minecraft:default")).withColor(MCCINametagMod.TEAM_COLOR))));
                }
                else {
                    topLabel = Text.empty();
                    bottomLabel = getStandardBottomText(true);
                }
            }
            else if (game == Game.ROCKET_SPLEEF_RUSH) {
                if (stage.equals("") || stage.equals("preRound")) {
                    topLabel = Text.empty();
                    bottomLabel = getStandardBottomText(false);
                }
                else if (stage.equals("inRound")) {
                    topLabel = Text.literal(UnicodeChars.TeamFlagBig).setStyle(Style.EMPTY.withColor(MCCINametagMod.FACTION_COLOR).withFont(new Identifier("mccinametagmod:mcci_icons")))
                            .append(Text.literal(" ").setStyle(Style.EMPTY.withColor(Formatting.WHITE)));

                    int maxDamage = player.getInventory().getArmorStack(2).getMaxDamage();
                    int currentDamage = maxDamage - player.getInventory().getArmorStack(2).getDamage();
                    int health = (int) Math.floor(((float) currentDamage) / maxDamage * 20);
                    if (Objects.equals(player.getInventory().getArmorStack(2).getName().getString(), "Air")) health = 20;

                    if (health >= 4) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 3) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_5).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 2) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 1) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar0_5).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar0_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));

                    topLabel = topLabel.append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                    if (health >= 8) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 7) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_5).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 6) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 5) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar0_5).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar0_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));

                    topLabel = topLabel.append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                    if (health >= 12) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 11) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_5).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 10) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 9) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar0_5).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar0_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));

                    topLabel = topLabel.append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                    if (health >= 16) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 15) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_5).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 14) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 13) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar0_5).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar0_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));

                    topLabel = topLabel.append(Text.literal("\uE002").setStyle(Style.EMPTY.withFont(new Identifier("mcc:negative_padding_nosplit"))));

                    if (health >= 20) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar2_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 19) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_5).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 18) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar1_0).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else if (health == 17) topLabel = topLabel.append(Text.literal(UnicodeChars.HealthBar0_5).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));
                    else topLabel = topLabel.append(Text.literal(switch ((int) health) {
                            case 16 -> UnicodeChars.HealthNum8_0;
                            case 15 -> UnicodeChars.HealthNum7_5;
                            case 14 -> UnicodeChars.HealthNum7_0;
                            case 13 -> UnicodeChars.HealthNum6_5;
                            case 12 -> UnicodeChars.HealthNum6_0;
                            case 11 -> UnicodeChars.HealthNum5_5;
                            case 10 -> UnicodeChars.HealthNum5_0;
                            case  9 -> UnicodeChars.HealthNum4_5;
                            case  8 -> UnicodeChars.HealthNum4_0;
                            case  7 -> UnicodeChars.HealthNum3_5;
                            case  6 -> UnicodeChars.HealthNum3_0;
                            case  5 -> UnicodeChars.HealthNum2_5;
                            case  4 -> UnicodeChars.HealthNum2_0;
                            case  3 -> UnicodeChars.HealthNum1_5;
                            case  2 -> UnicodeChars.HealthNum1_0;
                            case  1 -> UnicodeChars.HealthNum0_5;
                            default -> UnicodeChars.HealthBar0_0;
                        }).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(new Identifier("mccinametagmod:mcci_icons"))));

                    bottomLabel = getStandardBottomText(false);
                }
                else if (stage.equals("postGame")) {
                    topLabel = Text.empty();
                    bottomLabel = getStandardBottomText(false);
                }
                else {
                    topLabel = Text.empty();
                    bottomLabel = getStandardBottomText(true);
                }
            }
            else {
                topLabel = Text.literal(playerName).setStyle(Style.EMPTY.withColor(MCCINametagMod.COLOR));
                int crownLevel = player.experienceLevel;
                bottomLabel = Text.literal(MCCINametagMod.FACTION).setStyle(Style.EMPTY.withFont(new Identifier("mcc:icon")))
                        .append(Text.literal(MCCINametagMod.FACTION_LEVEL + " ").setStyle(Style.EMPTY.withFont(new Identifier("minecraft:default"))))
                        .append(Text.literal(MCCINametagMod.CROWN).setStyle(Style.EMPTY.withFont(new Identifier("mcc:icon"))))
                        .append(Text.literal(String.valueOf(crownLevel)).setStyle(Style.EMPTY.withColor(Formatting.YELLOW).withFont(new Identifier("minecraft:default"))));

                if (game == Game.PARKOUR_WARRIOR_LOBBY)
                {
                    Scoreboard scoreboard = MinecraftClient.getInstance().player.getScoreboard();
                    ScoreboardObjective obj = scoreboard.getObjectiveForSlot(ScoreboardDisplaySlot.SIDEBAR);
                    int medals = -1;
                    for (ScoreboardEntry entry : scoreboard.getScoreboardEntries(obj).stream().toList()) {
                        Team team = scoreboard.getScoreHolderTeam(entry.owner());
                        if (!team.getPrefix().getString().contains("Total Unique Medals")) continue;
                        medals = Integer.parseInt(team.getPrefix()
                                .getSiblings().get(0).getSiblings().get(0).getSiblings().get(0).getSiblings().get(0).getString().replace(",", ""));
                    }
                    bottomLabel = bottomLabel
                            .append(Text.literal(" " + UnicodeChars.MedalUnicode).setStyle(Style.EMPTY.withFont(new Identifier("mccinametagmod:mcci_icons"))))
                            .append(Text.literal(String.valueOf(medals)).setStyle(Style.EMPTY.withColor(Formatting.YELLOW).withFont(new Identifier("minecraft:default"))));
                }
            }
            rankLabel = Text.empty();
        } catch (Exception ex) {
            topLabel = Text.literal(playerName).setStyle(Style.EMPTY.withColor(Formatting.GRAY));
            bottomLabel = Text.literal("Error.").setStyle(Style.EMPTY.withColor(Formatting.DARK_RED));
            rankLabel = Text.empty();
            MCCINametagMod.LATEST_EXCEPTION = ex;
        }

        double d = this.dispatcher.getSquaredDistanceToCamera(entity);
        if (!(d > 4096.0)) {
            boolean bl = !entity.isSneaky();
            float f = entity.getHeight() + 1.0F;
            int i = "deadmau5".equals(bottomLabel.getString()) ? -10 : 0;
            matrices.push();
            matrices.translate(0.0, f, 0.0);
            matrices.multiply(this.dispatcher.getRotation());
            matrices.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = matrices.peek().getPositionMatrix();
            float g = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F);
            int j = (int)(g * 255.0F) << 24;
            TextRenderer textRenderer = this.getTextRenderer();
            float h = (float)(-textRenderer.getWidth(bottomLabel) / 2);
            textRenderer.draw(bottomLabel, h, (float)i, 553648127, false, matrix4f, vertexConsumers, bl ? TextRenderer.TextLayerType.SEE_THROUGH : TextRenderer.TextLayerType.NORMAL, j, light);
            if (bl) {
                textRenderer.draw(bottomLabel, h, (float)i, -1, false, matrix4f, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, light);
            }

            matrices.pop();

            if (!topLabel.getString().equals("")) {
                f = entity.getHeight() + 1.5F;
                i = "deadmau5".equals(topLabel.getString()) ? -10 : 0;
                matrices.push();
                matrices.translate(0.0, f, 0.0);
                matrices.multiply(getDispatcher().getRotation());
                matrices.scale(-0.025F, -0.025F, 0.025F);
                matrix4f = matrices.peek().getPositionMatrix();
                h = (float)(-textRenderer.getWidth(topLabel) / 2);
                textRenderer.draw(topLabel, h, (float) i, 553648127, false, matrix4f, vertexConsumers, bl ? TextRenderer.TextLayerType.SEE_THROUGH : TextRenderer.TextLayerType.NORMAL, j, light);
                if (bl) {
                    textRenderer.draw(topLabel, h, (float) i, -1, false, matrix4f, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, light);
                }

                matrices.pop();
            }

            if (!rankLabel.getString().equals("")) {
                f = entity.getHeight() + 2.0F;
                i = "deadmau5".equals(rankLabel.getString()) ? -10 : 0;
                matrices.push();
                matrices.translate(0.0, f, 0.0);
                matrices.multiply(getDispatcher().getRotation());
                matrices.scale(-0.025F, -0.025F, 0.025F);
                matrix4f = matrices.peek().getPositionMatrix();
                h = (float)(-textRenderer.getWidth(rankLabel) / 2);
                textRenderer.draw(rankLabel, h, (float) i, 553648127, false, matrix4f, vertexConsumers, bl ? TextRenderer.TextLayerType.SEE_THROUGH : TextRenderer.TextLayerType.NORMAL, j, light);
                if (bl) {
                    textRenderer.draw(rankLabel, h, (float) i, -1, false, matrix4f, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, light);
                }

                matrices.pop();
            }
        }
    }
}
