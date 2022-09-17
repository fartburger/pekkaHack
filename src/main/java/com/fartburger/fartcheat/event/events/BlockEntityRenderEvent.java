package com.fartburger.fartcheat.event.events;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.util.math.MatrixStack;

@SuppressWarnings("unused")
public class BlockEntityRenderEvent extends RenderEvent {

    final BlockEntity entity;

    public BlockEntityRenderEvent(MatrixStack stack, BlockEntity entity) {
        super(stack);
        this.entity = entity;
    }

    public BlockEntity getBlockEntity() {
        return entity;
    }
}
