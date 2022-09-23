package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleType;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

public class SettingGUI extends Module {
    int t = 2;

    public SettingGUI() {
        super("settinggui","a visual manager for all settings", ModuleType.RENDER);
        this.keybind.accept(GLFW.GLFW_KEY_0+"");

    }
    @Override
    public void tick() {
        t--;
        if(t==0) {
            FCRMain.client.setScreen(com.fartburger.fartcheat.gui.clickgui.SettingGUI.instance());
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
