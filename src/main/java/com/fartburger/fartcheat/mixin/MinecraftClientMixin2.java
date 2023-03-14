package com.fartburger.fartcheat.mixin;

import com.fartburger.fartcheat.config.ConfigManager;
import com.fartburger.fartcheat.event.EventType;
import com.fartburger.fartcheat.event.Events;
import com.fartburger.fartcheat.event.events.NonCancellableEvent;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin2 {
    @Inject(method = "stop", at = @At("HEAD"))
    void pekka_dispatchExit(CallbackInfo ci) {
        ConfigManager.saveState();
        Events.fireEvent(EventType.GAME_EXIT, new NonCancellableEvent());
    }
}
