package com.fartburger.fartcheat.mixin;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.config.ConfigManager;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleRegistry;
import com.fartburger.fartcheat.modules.hacks.Freecam;
import com.fartburger.fartcheat.modules.hacks.NoSlow;
import com.fartburger.fartcheat.modules.hacks.PortalGUI;
import com.fartburger.fartcheat.util.Utils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;


@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    private static int macIndex=0;
    @Inject(at=@At("HEAD"),method="tick")
    void pekka_tick(CallbackInfo ci) {
        Utils.TickManager.tick();
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
    @Inject(method = "pushOutOfBlocks", at = @At("HEAD"), cancellable = true)
    public void pekka_preventPush(double x, double z, CallbackInfo ci) {
        if (Objects.requireNonNull(ModuleRegistry.getByClass(Freecam.class)).isEnabled()) {
            ci.cancel();
        }
    }
    @Redirect(method = "updateNausea", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;shouldPause()Z"))
    public boolean pekka_overwritePauseScreen(Screen screen) {
        return Objects.requireNonNull(ModuleRegistry.getByClass(PortalGUI.class)).isEnabled() || screen.shouldPause();
    }
}
