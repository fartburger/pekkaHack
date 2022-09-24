package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.config.DoubleSetting;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleType;
import com.fartburger.fartcheat.util.Rotations;
import com.fartburger.fartcheat.util.Utils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;

import java.util.Objects;

public class SpinBot extends Module {
    int a=0;
    final DoubleSetting speed = this.config.create(new DoubleSetting.Builder(5).name("Delay")
            .description("How much to wait when spinning")
            .min(0)
            .max(100)
            .precision(0)
            .get());
    int timeout = 0;

    public SpinBot() {
        super("SpinBot", "Spins around like a maniac", ModuleType.MISC);
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
    }

    @Override
    public void onFastTick() {// timeout expired, set it back to full
        a++;
        Rotations.setClientYaw((float) a%360);
        PlayerMoveC2SPacket p1 = new PlayerMoveC2SPacket.LookAndOnGround(Rotations.getClientYaw(),
                Rotations.getClientPitch(),
                Objects.requireNonNull(FCRMain.client.player).isOnGround());
        FCRMain.client.getNetworkHandler().sendPacket(p1);
    }

    @Override
    public void onHudRender() {

    }
}
