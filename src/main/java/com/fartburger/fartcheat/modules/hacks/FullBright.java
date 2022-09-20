package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.mixinUtil.SimpleOptionDuck;
import com.fartburger.fartcheat.modules.ModuleType;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import com.fartburger.fartcheat.modules.Module;
import org.lwjgl.glfw.GLFW;

public class FullBright extends Module {

    double og;

    public FullBright() {
        super("FullBright", "Allows you to see in complete darkness", ModuleType.RENDER);
        this.keybind.accept(GLFW.GLFW_KEY_B+"");
    }

    @Override
    public void tick() {

    }

    @SuppressWarnings("unchecked")
    @Override
    public void enable() {
        og = MathHelper.clamp(FCRMain.client.options.getGamma().getValue(), 0, 1);
        // this somehow is a special case and i do not know why, this does work tho so im going to ignore it
        ((SimpleOptionDuck<Double>) (Object) FCRMain.client.options.getGamma()).setValueDirectly(10d);
    }

    @Override
    public void disable() {
        FCRMain.client.options.getGamma().setValue(og);
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

