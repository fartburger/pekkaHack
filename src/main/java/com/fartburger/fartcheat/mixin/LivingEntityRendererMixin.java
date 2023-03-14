package com.fartburger.fartcheat.mixin;

import com.fartburger.fartcheat.modules.ModuleRegistry;
import com.fartburger.fartcheat.modules.hacks.TrueSight;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

    @Redirect(at = @At(value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;isInvisibleTo(Lnet/minecraft/entity/player/PlayerEntity;)Z",
            ordinal = 0),
            method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
    private boolean pekka_cansee(LivingEntity instance, PlayerEntity playerEntity) {
        if(ModuleRegistry.getByClass(TrueSight.class).isEnabled()) return false;
        return instance.isInvisibleTo(playerEntity);
    }
}
