package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.event.EventListener;
import com.fartburger.fartcheat.event.EventType;
import com.fartburger.fartcheat.event.events.BlockEntityRenderEvent;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleType;
import com.fartburger.fartcheat.util.render.Renderer;
import net.minecraft.block.*;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class StorageESP extends Module {
    final List<BlockPos> positions = new CopyOnWriteArrayList<>();

    Color chestColor = new Color(143, 143, 0,225);
    Color shulkerBoxColor = new Color(138, 0, 148,185);
    Color netherPortalColor = new Color(208, 0, 255,200);
    Color endPortalColor = new Color(0,0,0,200);
    Color endPortalFrameColor = new Color(23, 255, 201,185);

    public StorageESP() {
        super("StorageESP", "Shows all chests in the area", ModuleType.RENDER);
    }

    void addIfNotExisting(BlockPos p) {
        if (positions.stream().noneMatch(blockPos -> blockPos.equals(p))) {
            positions.add(p);
        }
    }

    @EventListener(value = EventType.BLOCK_ENTITY_RENDER)
    void r(BlockEntityRenderEvent be) {
        if (!this.isEnabled()) {
            return;
        }
        if (be.getBlockEntity() instanceof ChestBlockEntity) {
            addIfNotExisting(be.getBlockEntity().getPos());
        }
        if(be.getBlockEntity() instanceof ShulkerBoxBlockEntity) {
            addIfNotExisting(be.getBlockEntity().getPos());
        }
        if(client.world.getBlockState(be.getBlockEntity().getPos()).getBlock() instanceof NetherPortalBlock) {
            addIfNotExisting(be.getBlockEntity().getPos());
        }
        if(client.world.getBlockState(be.getBlockEntity().getPos()).getBlock() instanceof EndPortalBlock) {
            addIfNotExisting(be.getBlockEntity().getPos());
        }
        if(client.world.getBlockState(be.getBlockEntity().getPos()).getBlock() instanceof EndPortalFrameBlock) {
            addIfNotExisting(be.getBlockEntity().getPos());
        }
    }

    @Override
    public void tick() {
        positions.removeIf(blockPos -> !(client.world.getBlockState(blockPos).getBlock() instanceof ChestBlock||client.world.getBlockState(blockPos).getBlock() instanceof ShulkerBoxBlock||client.world.getBlockState(blockPos).getBlock() instanceof NetherPortalBlock||client.world.getBlockState(blockPos).getBlock() instanceof EndPortalBlock||client.world.getBlockState(blockPos).getBlock() instanceof EndPortalBlock));
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public String getContext() {
        return null;
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {
        for (BlockPos position : positions) {
            if(client.world.getBlockState(position).getBlock() instanceof ChestBlock) {
                Renderer.R3D.renderFilled(matrices, chestColor, Vec3d.of(position), new Vec3d(1, 1, 1));
            }
            if(client.world.getBlockState(position).getBlock() instanceof ShulkerBoxBlock) {
                Renderer.R3D.renderFilled(matrices, shulkerBoxColor, Vec3d.of(position), new Vec3d(1, 1, 1));
            }
        }
    }

    @Override
    public void onHudRender() {

    }
}
