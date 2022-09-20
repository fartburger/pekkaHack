package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.config.DoubleSetting;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleType;
import com.fartburger.fartcheat.util.Timer;
import com.fartburger.fartcheat.util.render.Renderer;
import net.minecraft.block.Block;
import net.minecraft.client.input.Input;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.awt.*;
import java.util.Objects;

public class AutoLavacast extends Module {
    final DoubleSetting Height = this.config.create(new DoubleSetting.Builder(40).name("Height")
            .description("How high to build the lavacast")
            .min(10)
            .max(150)
            .precision(0)
            .get());
    static boolean moveForwards = false;
    final Timer timer = new Timer();
    Input original;
    Vec3i incr;
    BlockPos start;

    public AutoLavacast() {
        super("AutoLavacast", "Makes a lavacast", ModuleType.WORLD);
    }

    BlockPos getNextPosition() {
        int y = 0;
        while ((y + start.getY()) < start.getY()+Height.getValue()) {
            Vec3i ie = incr.multiply(y + 1);
            BlockPos next = start.add(ie).add(0, y, 0);
            if (FCRMain.client.world.getBlockState(next).getMaterial().isReplaceable()) {
                return next;
            }
            y++;
        }
        return null;
    }

    @Override
    public void onFastTick() {
        if (!timer.hasExpired(100)) {
            return;
        }
        timer.reset();
        BlockPos next = getNextPosition();
        if (next == null) {
            setEnabled(false);
            return;
        }
        Vec3d placeCenter = Vec3d.of(next).add(.5, .5, .5);

        if (placeCenter.distanceTo(FCRMain.client.player.getCameraPosVec(1)) < FCRMain.client.interactionManager.getReachDistance()) {
            moveForwards = false;

            ItemStack is = FCRMain.client.player.getInventory().getMainHandStack();
            if (is.isEmpty()) {
                return;
            }
            if (is.getItem() instanceof BlockItem bi) {
                Block p = bi.getBlock();
                if (p.getDefaultState().canPlaceAt(FCRMain.client.world, next)) {
                    FCRMain.client.execute(() -> {
                        BlockHitResult bhr = new BlockHitResult(placeCenter, Direction.DOWN, next, false);
                        FCRMain.client.interactionManager.interactBlock(FCRMain.client.player, Hand.MAIN_HAND, bhr);
                        Vec3d goP = Vec3d.of(next).add(0.5, 1.05, 0.5);
                        FCRMain.client.player.updatePosition(goP.x, goP.y, goP.z);
                    });
                }
            }

        } else {
            moveForwards = true;
        }
    }

    @Override
    public void tick() {

    }

    @Override
    public void enable() {
        if (original == null) {
            original = FCRMain.client.player.input;
        }
        incr = FCRMain.client.player.getMovementDirection().getVector();
        start = FCRMain.client.player.getBlockPos();
    }

    @Override
    public void disable() {
        FCRMain.client.player.input = original;
    }

    @Override
    public String getContext() {
        return null;
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {
        BlockPos next = getNextPosition();
        Renderer.R3D.renderOutline(matrices, Color.RED, Vec3d.of(start), new Vec3d(1, 0.01, 1));
        if (next != null) {
            Renderer.R3D.renderOutline(matrices, Color.BLUE, Vec3d.of(next), new Vec3d(1, 1, 1));
        }
    }

    @Override
    public void onHudRender() {

    }

    public enum Mode {
        Bypass, Fast
    }

    static class ListenInput extends Input {
        @Override
        public void tick(boolean slowDown, float f) {
            this.movementForward = moveForwards ? 1 : 0;
        }
    }
}