package com.fartburger.fartcheat.mixin;


import com.fartburger.fartcheat.modules.ModuleRegistry;
import com.fartburger.fartcheat.modules.hacks.Reach;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {


    @Inject(method = "getReachDistance", at = @At("HEAD"), cancellable = true)
    private void pekka_overwriteReach(CallbackInfoReturnable<Float> cir) {
        if (ModuleRegistry.getByClass(Reach.class).isEnabled()) {
            cir.setReturnValue((float) ModuleRegistry.getByClass(Reach.class).getReachDistance());
        }
    }

    @Inject(method = "hasExtendedReach", at = @At("HEAD"), cancellable = true)
    private void pekka_setExtendedReach(CallbackInfoReturnable<Boolean> cir) {
        if (ModuleRegistry.getByClass(Reach.class).isEnabled()) {
            cir.setReturnValue(true);
        }
    }

}
