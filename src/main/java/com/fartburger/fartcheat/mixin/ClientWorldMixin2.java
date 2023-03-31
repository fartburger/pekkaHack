package com.fartburger.fartcheat.mixin;

import com.fartburger.fartcheat.event.EventType;
import com.fartburger.fartcheat.event.Events;
import com.fartburger.fartcheat.event.events.EntityAddedEvent;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin2 {

    @Inject(method="addEntityPrivate",at=@At("TAIL"))
    void pekka_addentity(int id, Entity entity, CallbackInfo ci) {
        Events.fireEvent(EventType.ENTITY_ADDED,new EntityAddedEvent(entity));
    }
}
