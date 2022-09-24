package com.fartburger.fartcheat.event.events;

import net.minecraft.entity.Entity;

public class KilledEntityEvent extends NonCancellableEvent {
    private final Entity entity;

    public KilledEntityEvent(Entity entity) {
        this.entity = entity;
    }

    public boolean isEntityPlayer() { return entity.isPlayer(); }

    public Entity getEntity() {
        return entity;
    }
}
