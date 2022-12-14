package com.fartburger.fartcheat.util.vertex;

import com.fartburger.fartcheat.mixin.IRenderPhaseMixin;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DumpVertexProvider implements VertexConsumerProvider {
    static final Map<RenderLayer, VertexConsumer> dumps = new HashMap<>();

    public List<DumpVertexConsumer> getBuffers() {
        return new ArrayList<>(dumps.values()).stream()
                .filter(vertexConsumer -> vertexConsumer instanceof DumpVertexConsumer)
                .map(vertexConsumer -> (DumpVertexConsumer) vertexConsumer)
                .toList();
    }

    @Override
    public VertexConsumer getBuffer(RenderLayer layer) {
        return dumps.computeIfAbsent(layer, renderLayer -> {
            if (((IRenderPhaseMixin) renderLayer).getName().startsWith("entity")) {
                return new DumpVertexConsumer();
            } else {
                return new DiscardingVertexConsumer();
            }
        });
    }
}
