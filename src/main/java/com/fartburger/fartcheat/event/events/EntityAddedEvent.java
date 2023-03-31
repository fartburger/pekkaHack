package com.fartburger.fartcheat.event.events;

import net.minecraft.entity.Entity;

public class EntityAddedEvent extends NonCancellableEvent {
    private final Entity entity;

    public boolean isEntityPlayer() {
        return entity.isPlayer();
    }

    public EntityAddedEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

}
