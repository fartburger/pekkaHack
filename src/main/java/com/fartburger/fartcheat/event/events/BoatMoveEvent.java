package com.fartburger.fartcheat.event.events;

import net.minecraft.entity.vehicle.BoatEntity;

public class BoatMoveEvent extends Event {
    public final BoatEntity boat;

    public BoatMoveEvent(BoatEntity boat) {
        this.boat = boat;
    }
}
