package me.anastarawneh.mccinametagmod.mixin;

import me.anastarawneh.mccinametagmod.util.LivingEntityRenderStateExtension;
//? if >=1.21.2
/*import net.minecraft.client.render.entity.state.LivingEntityRenderState;*/
import org.spongepowered.asm.mixin.Mixin;

//? if >=1.21.2
/*@Mixin(LivingEntityRenderState.class)*/
public class LivingEntityRenderStateMixin implements LivingEntityRenderStateExtension {
    public boolean applyLabel;

    @Override
    public boolean getApplyLabel() {
        return applyLabel;
    }

    @Override
    public void setApplyLabel(boolean applyLabel) {
        this.applyLabel = applyLabel;
    }
}
