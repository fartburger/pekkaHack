package com.fartburger.fartcheat.mixin;

import com.fartburger.fartcheat.mixinUtil.IClientPlayerInteractionManager;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientPlayerInteractionManager.class)
public interface IClientPlayerInteractionManagerMixin{

    @Accessor("currentBreakingPos")
    BlockPos getCurrentBreakingPos();


}
