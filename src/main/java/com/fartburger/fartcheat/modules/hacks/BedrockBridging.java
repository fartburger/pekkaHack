package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.event.EventType;
import com.fartburger.fartcheat.event.Events;
import com.fartburger.fartcheat.event.events.MouseEvent;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleRegistry;
import com.fartburger.fartcheat.modules.ModuleType;
import com.fartburger.fartcheat.util.Utils;
import com.fartburger.fartcheat.util.render.Renderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.awt.*;

public class BedrockBridging extends Module {

    public BedrockBridging() {
        super("BedrockBridging","Lets you bridge like in bedrock edition", ModuleType.MOVEMENT);

        Events.registerEventHandler(EventType.MOUSE_EVENT, event -> {
            if (!this.isEnabled()) {
                return;
            }
            if (ModuleRegistry.getByClass(BedrockBridging.class).isEnabled() && ((MouseEvent) event).getButton() == 2 && ((MouseEvent) event).getAction() == 0) {
                if (FCRMain.client.currentScreen != null) {
                    return;
                }
            }
            try {
                if (!FCRMain.client.world.getBlockState(FCRMain.client.player.getBlockPos().subtract(new Vec3i(0, 1, 0)).add(FCRMain.client.player.getMovementDirection().getVector())).isAir()) {
                    return;
                }
                FCRMain.client.interactionManager.interactBlock(FCRMain.client.player,Hand.MAIN_HAND,
                        new BlockHitResult(Vec3d.of(FCRMain.client.player.getBlockPos().subtract(new Vec3i(0, 1, 0)).add(FCRMain.client.player.getMovementDirection().getVector())),
                                Direction.DOWN,
                                FCRMain.client.player.getBlockPos().subtract(new Vec3i(0, 1, 0)).add(FCRMain.client.player.getMovementDirection().getVector()),false));
                Renderer.R3D.renderFadingBlock(Renderer.Util.modify(Utils.getCurrentRGB(), -1, -1, -1, 255),
                        Renderer.Util.modify(Utils.getCurrentRGB(), -1, -1, -1, 100).darker(),
                        Vec3d.of(FCRMain.client.player.getBlockPos().subtract(new Vec3i(0, 1, 0)).add(FCRMain.client.player.getMovementDirection().getVector())),
                        new Vec3d(1, 1, 1),
                        1000);
                return;
            } catch(Exception ignored) {}

        },0);
    }


    @Override
    public void tick() {

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
        if (!MinecraftClient.getInstance().world.getBlockState(FCRMain.client.player.getBlockPos().subtract(new Vec3i(0, 1, 0)).add(FCRMain.client.player.getMovementDirection().getVector())).isAir()) {
            return;
        }
        if ((MinecraftClient.getInstance().player.getMainHandStack().getItem() instanceof BlockItem)) {
            Renderer.R3D.renderOutline(matrices,new Color(5, 23, 86),
                    Vec3d.of(FCRMain.client.player.getBlockPos().subtract(new Vec3i(0, 1, 0)).add(FCRMain.client.player.getMovementDirection().getVector())),
                    new Vec3d(1, 1, 1));
        }
    }

    @Override
    public void onHudRender() {

    }
}
