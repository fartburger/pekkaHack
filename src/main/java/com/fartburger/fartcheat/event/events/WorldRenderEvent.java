package com.fartburger.fartcheat.event.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.util.math.MatrixStack;

@RequiredArgsConstructor
public class WorldRenderEvent extends NonCancellableEvent {
    @Getter
    final MatrixStack contextStack;
}
