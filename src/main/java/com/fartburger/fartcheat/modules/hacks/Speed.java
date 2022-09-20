package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.config.DoubleSetting;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleType;
import net.minecraft.client.util.math.MatrixStack;

public class Speed extends Module {

    public final DoubleSetting speed = this.config.create(new DoubleSetting.Builder(3).name("Speed")
            .description("The speed multiplier to apply")
            .min(1)
            .max(10)
            .precision(3)
            .get());

    public Speed() {
        super("Speed", "Gives you an extreme speed boost", ModuleType.MOVEMENT);
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
}
