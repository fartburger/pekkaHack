package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.config.DoubleSetting;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

import java.util.Objects;

public class Step extends Module {

    final DoubleSetting height = this.config.create(new DoubleSetting.Builder(3).name("Height").description("How high to step").min(1).max(50).precision(0).get());

    public Step() {
        super("Step", "Allows you to step up full blocks", ModuleType.MOVEMENT);
    }

    @Override
    public void tick() {
        if (FCRMain.client.player == null || FCRMain.client.getNetworkHandler() == null) {
            return;
        }
        FCRMain.client.player.setStepHeight((float) (height.getValue() + 0));
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {
        if (FCRMain.client.player == null || FCRMain.client.getNetworkHandler() == null) {
            return;
        }
        Objects.requireNonNull(MinecraftClient.getInstance().player).setStepHeight(0.6f);
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
