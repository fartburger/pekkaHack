package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.event.EventType;
import com.fartburger.fartcheat.event.Events;
import com.fartburger.fartcheat.event.events.MouseEvent;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleType;
import com.fartburger.fartcheat.util.Utils;
import com.fartburger.fartcheat.util.render.Renderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

public class AirPlace extends Module {

    boolean enabled = false;

    public AirPlace() {
        super("AirPlace", "Places blocks in the air", ModuleType.MISC);
        Events.registerEventHandler(EventType.MOUSE_EVENT, event -> {
            if (!this.isEnabled()) {
                return;
            }
            if (enabled && ((MouseEvent) event).getButton() == 1 && ((MouseEvent) event).getAction() == 1) {
                if (FCRMain.client.currentScreen != null) {
                    return;
                }
                try {
                    if (!client.world.getBlockState(((BlockHitResult) FCRMain.client.crosshairTarget).getBlockPos()).isAir()) {
                        return;
                    }
                    FCRMain.client.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND,
                            (BlockHitResult) FCRMain.client.crosshairTarget,
                            Utils.increaseAndCloseUpdateManager(FCRMain.client.world)));
                    if ((client.player.getMainHandStack().getItem() instanceof BlockItem)) {
                        Renderer.R3D.renderFadingBlock(Renderer.Util.modify(Utils.getCurrentRGB(), -1, -1, -1, 255),
                                Renderer.Util.modify(Utils.getCurrentRGB(), -1, -1, -1, 100).darker(),
                                Vec3d.of(((BlockHitResult) FCRMain.client.crosshairTarget).getBlockPos()),
                                new Vec3d(1, 1, 1),
                                1000);
                    }
                    FCRMain.client.player.swingHand(Hand.MAIN_HAND);
                    event.setCancelled(true);
                } catch (Exception ignored) {
                }
            }
        }, 0);
    }

    @Override
    public void tick() {

    }

    @Override
    public void enable() {
        enabled = true;
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
        if (!client.world.getBlockState(((BlockHitResult) FCRMain.client.crosshairTarget).getBlockPos()).isAir()) {
            return;
        }
        if ((client.player.getMainHandStack().getItem() instanceof BlockItem)) {
            Renderer.R3D.renderOutline(matrices,new Color(5, 23, 86),
                    Vec3d.of(((BlockHitResult) FCRMain.client.crosshairTarget).getBlockPos()),
                    new Vec3d(1, 1, 1));
        }
    }

    @Override
    public void onHudRender() {

    }
}
