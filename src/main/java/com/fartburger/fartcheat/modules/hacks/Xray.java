package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.event.EventListener;
import com.fartburger.fartcheat.event.EventType;
import com.fartburger.fartcheat.event.events.BlockRenderEvent;
import com.fartburger.fartcheat.event.events.ChunkRenderQueryEvent;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleRegistry;
import com.fartburger.fartcheat.modules.ModuleType;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.block.RedstoneOreBlock;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.registry.Registries;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class Xray extends Module {

    public static final List<Block> blocks = Lists.newArrayList();

    public Xray() {
        super("Xray", "Allows you to see ores through blocks", ModuleType.RENDER);
        this.keybind.accept(GLFW.GLFW_KEY_X+"");
        Registries.BLOCK.forEach(block -> {
            if (blockApplicable(block)) {
                blocks.add(block);
            }
        });
    }

    boolean blockApplicable(Block block) {
        boolean c1 = block == Blocks.CHEST || block == Blocks.FURNACE || block == Blocks.END_GATEWAY || block == Blocks.COMMAND_BLOCK || block == Blocks.ANCIENT_DEBRIS;
        boolean c2 = block instanceof ExperienceDroppingBlock || block instanceof RedstoneOreBlock;
        return c1 || c2;
    }

    @EventListener(value = EventType.BLOCK_RENDER)
    void blockRender(BlockRenderEvent bre) {
        if (!blockApplicable(bre.getBlockState().getBlock())) {
            bre.setCancelled(true);
        }
    }

    @EventListener(value = EventType.SHOULD_RENDER_CHUNK)
    void shouldRenderChunk(ChunkRenderQueryEvent event) {
        event.setShouldRender(true);
    }

    @Override
    public void tick() {

    }

    @Override
    public void enable() {
        FCRMain.client.worldRenderer.reload();
        if(!ModuleRegistry.getByClass(FullBright.class).isEnabled()) {
            ModuleRegistry.getByClass(FullBright.class).enable();
        }
    }

    @Override
    public void disable() {
        FCRMain.client.worldRenderer.reload();
    }

    @Override
    public String getContext() {
        return null;
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {

    }

    @Override
    public void onHudRender() {

    }
}
