package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.config.DoubleSetting;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleType;
import net.minecraft.client.util.math.MatrixStack;

public class Reach extends Module {
    final DoubleSetting reachDist = this.config.create(new DoubleSetting.Builder(3).min(3).max(10).precision(1).name("Distance").description("How far to reach").get());

    public Reach() {
        super("Reach", "Reach further", ModuleType.COMBAT);
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
    public void onHudRender() {

    }

    public double getReachDistance() {
        return reachDist.getValue();
    }
}