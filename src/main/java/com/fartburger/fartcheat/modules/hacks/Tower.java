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
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.awt.*;

public class Tower extends Module {

    final DoubleSetting height = this.config.create(new DoubleSetting.Builder(40).name("Height")
            .description("how high to build up")
            .min(3)
            .max(64)
            .precision(0)
            .get());

    BlockPos start;
    BlockPos lastknown;

    Vec3i incr;

    final Timer timer = new Timer();

    Input original;

    static boolean goup = false;

    public Tower() {
        super("Tower","builds straight up really fast", ModuleType.MOVEMENT);
    }

    BlockPos getNextPosition() {
        int y = 0;
        while ((y + start.getY()) < start.getY()+height.getValue()) {
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
            Vec3d d = Vec3d.of(lastknown).add(0,1.05,0);
            FCRMain.client.player.updatePosition(d.x,d.y,d.z);
            setEnabled(false);
            return;
        }
        Vec3d placeCenter = Vec3d.of(next).add(0, .5, 0);

        if (placeCenter.distanceTo(FCRMain.client.player.getCameraPosVec(1)) < FCRMain.client.interactionManager.getReachDistance()) {
            goup = false;

            ItemStack is = FCRMain.client.player.getInventory().getMainHandStack();
            if (is.isEmpty()) {
                Vec3d d = Vec3d.of(next).add(0,1.05,0);
                FCRMain.client.player.updatePosition(d.x,d.y,d.z);
                FCRMain.client.player.sendMessage(Text.of(Formatting.RED+"you ran out of blocks idiot"));
                setEnabled(false);
                return;
            }
            if (is.getItem() instanceof BlockItem bi) {
                Block p = bi.getBlock();
                if (p.getDefaultState().canPlaceAt(FCRMain.client.world, next)) {
                    FCRMain.client.execute(() -> {
                        Vec3d goP = Vec3d.of(next).add(0, 1.05, 0);
                        FCRMain.client.player.updatePosition(goP.x+0.4, goP.y+2, goP.z+0.4);
                        BlockHitResult bhr = new BlockHitResult(placeCenter, Direction.DOWN, next, false);
                        FCRMain.client.interactionManager.interactBlock(FCRMain.client.player, Hand.MAIN_HAND, bhr);
                        FCRMain.client.player.setVelocity(0,0,0);
                        lastknown = next;
                    });
                }
            }

        } else {
            goup = true;
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
        incr = FCRMain.client.player.getMovementDirection().getVector().subtract(new Vec3i(FCRMain.client.player.getMovementDirection().getVector().getX(),0,FCRMain.client.player.getMovementDirection().getVector().getZ()));
        start = FCRMain.client.player.getBlockPos();
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
        BlockPos next = getNextPosition();
        Renderer.R3D.renderOutline(matrices, Color.RED, Vec3d.of(start), new Vec3d(1, 0.01, 1));
        if (next != null) {
            Renderer.R3D.renderOutline(matrices, Color.BLUE, Vec3d.of(next), new Vec3d(1, 1, 1));
        }
    }

    @Override
    public void onHudRender() {

    }

    static class ListenInput extends Input {
        @Override
        public void tick(boolean slowDown, float f) {
            this.movementForward = goup ? 1 : 0;
        }
    }
}
