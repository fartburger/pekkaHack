package com.fartburger.fartcheat.mixin;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.config.ConfigManager;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleRegistry;
import com.fartburger.fartcheat.modules.hacks.NoSlow;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @Inject(at=@At("HEAD"),method="tick")
    void pekka_tick(CallbackInfo ci) {
        if (!ConfigManager.enabled) {
            ConfigManager.enableModules();
        }
        for (Module module : ModuleRegistry.getModules()) {
            if (module.isEnabled()) {
                module.tick();
            }
        }
    }
    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"))
    boolean pekka_noSlow(ClientPlayerEntity instance) {
        NoSlow noSlow = ModuleRegistry.getByClass(NoSlow.class);
        if (this.equals(FCRMain.client.player) && noSlow.isEnabled()) {
            return false;
        }
        return instance.isUsingItem();
    }
}
