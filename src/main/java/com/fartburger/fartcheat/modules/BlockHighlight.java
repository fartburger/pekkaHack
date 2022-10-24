package com.fartburger.fartcheat.modules;

import com.fartburger.fartcheat.config.StringSetting;
import com.fartburger.fartcheat.event.EventListener;
import com.fartburger.fartcheat.event.EventType;
import com.fartburger.fartcheat.event.events.BlockRenderEvent;
import com.fartburger.fartcheat.util.render.Renderer;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.*;
import java.util.List;

public class BlockHighlight extends Module {

    public static List<BlockPos> toHighlight;

    StringSetting blocks = this.config.create(new StringSetting.Builder("").name("Blocks")
            .description("What blocks to highlight, seperated by a semicolon(ex: diamond_ore;ancient_debris)")
            .get());

    public BlockHighlight() {
        super("BlockHighlight","Highlights blocks",ModuleType.RENDER);
    }

    @EventListener(EventType.BLOCK_RENDER)
    void blockRenderEvent(BlockRenderEvent br) {
        if(Objects.equals(blocks.getValue(), "")) return;
        List<String> bs2h = new ArrayList<>(Arrays.asList(blocks.getValue().split(";")));
        for (String s : bs2h) {
            if(Objects.equals(br.getBlockState().getBlock().getName().getContent().toString(), s)) {
                addIfNotExists(br.getPosition());
            }
        }
        List<Block> b2h = new ArrayList<>();
    }

    public void addIfNotExists(BlockPos bp) {
        if(toHighlight.stream().noneMatch(blockpos -> blockpos.equals(bp))) {
            toHighlight.add(bp);
        }
    }


    @Override
    public void tick() {
        try {
            toHighlight.forEach(bp -> {
                if(client.world.getBlockState(bp)==null) {
                    toHighlight.remove(bp);
                }
            });
        } catch (Exception ignored) {}
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
        try {
            toHighlight.forEach(bp -> {
                Renderer.R3D.renderFilled(matrices, new Color(30, 132, 213, 102),
                        new Vec3d(bp.getX(), bp.getY(), bp.getZ()),
                        new Vec3d(1, 1, 1));
            });
        } catch(Exception ignored) {}
    }

    @Override
    public void onHudRender() {

    }
}
