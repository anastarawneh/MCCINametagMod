package me.anastarawneh.mccinametagmod.mixin;

import me.anastarawneh.mccinametagmod.MCCINametagMod;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GenericContainerScreen.class)
public class GenericContainerScreenMixin {
    @Unique private Inventory inventory;
    @Unique private Text title;
    @Unique boolean processed = false;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void constructor(GenericContainerScreenHandler handler, PlayerInventory inventory, Text title, CallbackInfo ci) {
        this.inventory = handler.getInventory();
        this.title = title;
        this.processed = false;
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!processed && MCCINametagMod.modEnabled()) {
            if (title.getString().contains("MY PROFILE")) {
                try {
                    if (inventory.getStack(49).getName().getString().equals("Air")) return;
                    String levelLine = inventory.getStack(49).getComponents().get(DataComponentTypes.LORE).lines().get(2).getString();
                    MCCINametagMod.LOGGER.info(levelLine);
                    String level = levelLine.split(" ")[1];
                    if (MCCINametagMod.FACTION_LEVEL != Integer.parseInt(level)) {
                        MCCINametagMod.FACTION_LEVEL = Integer.parseInt(level);
                        MCCINametagMod.getConfig().factionLevel = Integer.parseInt(level);
                        MCCINametagMod.saveConfig();
                        MCCINametagMod.sendChatMessage(Text.literal("Saved faction level (" + level + ").").setStyle(Style.EMPTY.withColor(Formatting.GREEN)));
                    }
                } catch (Exception ex) {
                    MCCINametagMod.sendChatMessage(Text.literal("Could not retrieve current faction level. Check the log for more details.").setStyle(Style.EMPTY.withColor(Formatting.RED)));
                    MCCINametagMod.LOGGER.throwing(ex);
                }
            }
            processed = true;
        }
    }
}
