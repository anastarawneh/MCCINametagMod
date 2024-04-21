package me.anastarawneh.mccinametagmod.mixin;

import me.anastarawneh.mccinametagmod.MCCINametagMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GenericContainerScreen.class)
public class GenericContainerScreenMixin {
    @Shadow @Final private int rows;
    @Unique private GenericContainerScreenHandler handler;
    @Unique private Inventory inventory;
    @Unique private Text title;
    @Unique boolean processed = false;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void constructor(GenericContainerScreenHandler handler, PlayerInventory inventory, Text title, CallbackInfo ci) {
        this.handler = handler;
        this.inventory = handler.getInventory();
        this.title = title;
        this.processed = false;
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!processed) {
            if (title.getString().contains("WARDROBE") && title.getString().contains("PROFILE")) {
                if (inventory.getStack(49).getName().getString().equals("Air")) return;
                NbtList lore = inventory.getStack(49).getNbt().getCompound("display").getList("Lore", NbtElement.STRING_TYPE);
                String levelLine = lore.get(2).asString();
                String level = JsonHelper.deserialize(levelLine).getAsJsonArray("extra").get(4).getAsJsonObject().get("text").getAsString();
                if (MCCINametagMod.FACTION_LEVEL != Integer.parseInt(level)) {
                    MCCINametagMod.FACTION_LEVEL = Integer.parseInt(level);
                    MCCINametagMod.getConfig().factionLevel = Integer.parseInt(level);
                    MCCINametagMod.saveConfig();
                    if (MCCINametagMod.modEnabled()) MinecraftClient.getInstance().player.sendMessage(MCCINametagMod.MESSAGE_PREFIX.copy().append(Text.literal("Saved faction level (" + level + ").").setStyle(Style.EMPTY.withColor(Formatting.GREEN))));
                }
            }
            processed = true;
        }
    }
}
