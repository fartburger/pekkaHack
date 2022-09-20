package com.fartburger.fartcheat.mixin;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.modules.ModuleRegistry;
import com.fartburger.fartcheat.modules.hacks.Jesus;
import com.fartburger.fartcheat.modules.hacks.NoLevitation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(at=@At("HEAD"),method="canWalkOnFluid",cancellable = true)
    void pekka_liquids(FluidState state, CallbackInfoReturnable<Boolean> cir) {
        if(this.equals(FCRMain.client.player)) {
            if (ModuleRegistry.getByClass(Jesus.class).isEnabled()) {
                cir.setReturnValue(true);
            }
        }
    }
    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "net/minecraft/entity/LivingEntity.hasStatusEffect(Lnet/minecraft/entity/effect/StatusEffect;)Z"), require = 0)
    boolean pekka_nolevitation(LivingEntity instance, StatusEffect effect) {
        if (instance.equals(FCRMain.client.player) && ModuleRegistry.getByClass(NoLevitation.class).isEnabled() && effect == StatusEffects.LEVITATION) {
            return false;
        } else {
            return instance.hasStatusEffect(effect);
        }
    }
}
