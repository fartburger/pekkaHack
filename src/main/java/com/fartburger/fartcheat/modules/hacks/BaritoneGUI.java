package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleType;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

public class BaritoneGUI extends Module {
    int t=2;

    public BaritoneGUI() {
        super("BaritoneGUI","gui for baritone", ModuleType.RENDER);
        this.keybind.accept(GLFW.GLFW_KEY_SEMICOLON+"");
    }


    @Override
    public void tick() {
        t--;
        if (t == 0) {
            FCRMain.client.setScreen(com.fartburger.fartcheat.gui.clickgui.BaritoneGUI.instance());
            setEnabled(false);
        }
    }

    @Override
    public void enable() {
        t=2;
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
