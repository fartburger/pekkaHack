package com.fartburger.fartcheat.modules.hacks;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleType;
import com.fartburger.fartcheat.util.Utils;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class BaritoneGUI extends Module {
    int t=2;

    public BaritoneGUI() {
        super("BaritoneGUI","gui for baritone", ModuleType.RENDER);
        this.keybind.accept(GLFW.GLFW_KEY_SEMICOLON+"");
    }

    public static boolean runMacro = false;
    public static List<String> macrosToRun = new ArrayList<>();
    public static int macIndex = 0;


    @Override
    public void tick() {
        t--;
        if (t == 0) {
            FCRMain.client.setScreen(com.fartburger.fartcheat.gui.clickgui.BaritoneGUI.instance());
            setEnabled(false);
        }
        runMacro = com.fartburger.fartcheat.gui.clickgui.BaritoneGUI.runMacro;
        macrosToRun = com.fartburger.fartcheat.gui.clickgui.BaritoneGUI.macrosToRun;

        if(runMacro) {
            if(macIndex<macrosToRun.size()) {
                IBaritone bar = BaritoneAPI.getProvider().getPrimaryBaritone();
                if(!bar.getPathingBehavior().isPathing()&&!bar.getGetToBlockProcess().isActive()&&!bar.getBuilderProcess().isActive()&&!bar.getExploreProcess().isActive()&&!bar.getMineProcess().isActive()&&!bar.getMineProcess().isActive()) {
                    bar.getCommandManager().execute(macrosToRun.get(macIndex));
                    Utils.chatLog("Executing baritone command: "+ macrosToRun.get(macIndex));
                    macIndex++;
                }
            } else {
                runMacro = false;
                macIndex=0;
            }
        }
    }

    @Override
    public void enable() {
        t=2;
        com.fartburger.fartcheat.gui.clickgui.BaritoneGUI.initButtons();
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
