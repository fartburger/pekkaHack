package com.fartburger.fartcheat.mixin;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.modules.ModuleRegistry;
import com.fartburger.fartcheat.modules.hacks.SnowWalk;
import net.minecraft.block.PowderSnowBlock;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PowderSnowBlock.class)
public class PowderSnowBlockMixin {
    @Inject(at = @At("HEAD"), method = "canWalkOnPowderSnow(Lnet/minecraft/entity/Entity;)Z", cancellable = true)
    private static void pekka_snowwalk(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if(!ModuleRegistry.getByClass(SnowWalk.class).isEnabled()) return;
        if(entity!= FCRMain.client.player) return;
        cir.setReturnValue(true);
    }


}
