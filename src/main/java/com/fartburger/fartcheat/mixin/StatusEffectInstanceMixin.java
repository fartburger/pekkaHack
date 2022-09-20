package com.fartburger.fartcheat.mixin;

import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StatusEffectInstance.class)
public interface StatusEffectInstanceMixin {
    @Accessor("duration")
    void setDuration(int duration);

    @Accessor("amplifier")
    void setAmplifier(int amplifier);
}
