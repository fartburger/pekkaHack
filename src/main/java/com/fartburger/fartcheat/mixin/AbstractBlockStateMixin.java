package com.fartburger.fartcheat.mixin;

import com.fartburger.fartcheat.modules.ModuleRegistry;
import com.fartburger.fartcheat.modules.hacks.Xray;
import net.minecraft.block.AbstractBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(AbstractBlock.AbstractBlockState.class)
public class AbstractBlockStateMixin {

    @Inject(method = "getLuminance", at = @At("HEAD"), cancellable = true)
    public void pekka_luminateBlock(CallbackInfoReturnable<Integer> cir) {
        if (Objects.requireNonNull(ModuleRegistry.getByClass(Xray.class)).isEnabled()) {
            cir.setReturnValue(15);
        }
    }
}
