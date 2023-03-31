package com.fartburger.fartcheat.mixin;

import com.fartburger.fartcheat.mixinUtil.IClientPlayerInteractionManager;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class IClientPlayerInteractionManagerMixin2 implements IClientPlayerInteractionManager {
    @Shadow
    protected abstract void syncSelectedSlot();

    @Override
    public void syncSelected() {
        syncSelectedSlot();
    }
}
